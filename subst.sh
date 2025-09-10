#!/usr/bin/env bash
# shellcheck disable=SC2016

. "$(dirname "$(readlink -f "$0")")/../scripts/utils.sh"

_EVEN_DOLLARS_PATTERN='(?:\$\$)+'
_SINGLE_QUOTED_STRING_PATTERN="'[^\\']*(\\['nrtb\\][^\\']*)*'"
_DOUBLE_QUOTED_STRING_PATTERN='"[^\\"]*(\\["nrtb\\][^\\"]*)*"'
_ID_PATTERN='[_\p{L}][_\p{L}\p{N}]*'
_INTERPOLATE_PATTERN='\$('$_ID_PATTERN')'
_KEY_PATTERN='(?:'"$_ID_PATTERN"'|\d+|'"$_DOUBLE_QUOTED_STRING_PATTERN"')'
_INTERPOLATE_BRACED_PATTERN='\$\{\s*('"$_KEY_PATTERN"'(?:\s*\.\s*'"$_KEY_PATTERN"')*)\s*\}'
_EVALUATE_START_PATTERN='\\*\{'
_OTHER_PATTERN='[^$\\{]}+'

token_match() {
    local string=$1 regex=$2
    perl -CS -e '
        use strict;
        use warnings;
        my ($string, $regex) = @ARGV;
        if ($string =~ /^($regex)/) {
            print $&;
        }
    ' "$string" "$regex"
}

function bash_env() {
  local full_key=$1
  local key="${full_key%%-*}"
  local default="${full_key#*-}"

  if [[ -v $key ]]; then
    printf "%s" "${!key}"
    return 0
  elif [[ "$full_key" == *-* ]]; then
    printf "%s" "$default"
    return 0
  fi

  return 1
}

#+-------------------------+
#| while rest not empty     |
#+-------------------------+
#          |
#          v
#+-------------------------+
#| Check EVEN_DOLLARS      |
#|   regex: (?:\$\$)+      |
#+-------------------------+
#  if match:
#    - convert to literal $ if esc_interpolate=true
#    - append to output
#    - advance rest
#          |
#          v
#+-------------------------+
#| Check simple $IDENT     |
#|   regex: \$\w+          |
#+-------------------------+
#  if match:
#    - call get_value([IDENT])
#    - deep interpolate if enabled
#    - append to output
#    - advance rest
#          |
#          v
#+-------------------------+
#| Check braced ${KEYS}    |
#|   regex: \${(_KEY(\s*\.\s*_KEY)*)} |
#+-------------------------+
#  if match:
#    - parse keys (quotes stripped)
#    - call get_value(keys_array)
#    - deep interpolate if enabled
#    - append to output
#    - advance rest
#          |
#          v
#+-------------------------+
#| Check evaluation {      |
#|   regex: (\\*\{)        |
#+-------------------------+
#  if match:
#    - count leading backslashes
#    - if even → literal `{`
#    - if odd → treat as expression start
#    - append converted value
#    - advance rest
#          |
#          v
#+-------------------------+
#| Else: consume any other |
#|   regex: [^$\\]+        |
#+-------------------------+
#  if match:
#    - append to output
#    - advance rest
#          |
#          v
#+-------------------------+
#| Consume 1 char if nothing matched
#+-------------------------+
#          |
#          v
#         loop
function substitute_string() {
    local interpolate=false
    local interpolate_braced=true
    local deep_interpolate=true
    local esc_interpolate=true
    local evaluate=true
    local esc_evaluate=true

    while [[ "$1" == -* ]]; do
        case "$1" in
            -i|--interpolate) interpolate="${2:-true}"; shift 2 ;;
            -ib|--interpolate_braced) interpolate_braced="${2:-true}"; shift 2 ;;
            -di|--deep-interpolate) deep_interpolate="${2:-true}"; shift 2 ;;
            -ei|--escape-interpolate) esc_interpolate="${2:-true}"; shift 2 ;;
            -e|--evaluate) evaluate="${2:-true}"; shift 2 ;;
            -ee|--escape-evaluate) esc_evaluate="${2:-true}"; shift 2 ;;
            *) echo "Unknown option $1"; shift ;;
        esac
    done

    local getter=${1:-bash_env}
    local evaluator=${2:-bash_c}
    local source
    local output=""
    local rest

    source=$(src "${3:-}")
    rest=$source

    # shellcheck disable=SC2317
    function inner_evaluator() {
        echo "$2"
    }

    # Process string
    while [[ -n "$rest" ]]; do
        local match
        local len
        if [[ "$interpolate" == true || "$interpolate_braced" == true ]]; then
            match=$(token_match "$rest" "$_EVEN_DOLLARS_PATTERN")
            if [[ -n "$match" ]]; then
                len=${#match}
                [[ "$esc_interpolate" == true ]] && match="${match:0:$((len/2))}"
                output+="$match"
                rest="${rest:$len}"
                continue
            fi
        fi

        if [[ "$interpolate" == true ]]; then
            match=$(perl -e 'my ($s) = @ARGV; if($s =~ /^\$(\w+)/) { print $1; }' "$rest")
            if [[ -n "$match" ]]; then
                local -a path=("$match")
                local value
                value=$("$getter" path)
                local status=$?
                if [[ $status == 0 && "$deep_interpolate" == true ]]; then
                    value=$(substitute_string -i "$interpolate" -ib "$interpolate_braced" -di "$deep_interpolate" \
                        -ei "$esc_interpolate" -e "$evaluate" -ee "$esc_evaluate" "$getter" "$evaluator" <<< "$value")
                fi

                output+="$value"
                rest="${rest:${#match}+1}"
                continue
            fi
        fi

        if [[ "$interpolate_braced" == true ]]; then
            match=$(perl -MJSON -CS -e '
                my ($s, $regex, $kr) = @ARGV;
                if ($s =~ /^($regex)/) {
                    my $full = $1;
                    my $len = length($&);
                    my @keys;
                    while ($full =~ /$kr/g) {
                        my $key = $&;
                        $key =~ s/^"(.*)"$/$1/;
                        push @keys, $key;
                    }
                    print "$len\n" . encode_json(\@keys);
                }
            ' "$rest" "$_INTERPOLATE_BRACED_PATTERN" "$_KEY_PATTERN")

            if [[ -n "$match" ]]; then
                len="${match%%$'\n'*}"
                mapfile -t path < <(echo "${match#*$'\n'}" | jq -r '.[]')
                local value
                value=$("$getter" path)

                if [[ "$deep_interpolate" == true ]]; then
                    value=$(substitute_string -i "$interpolate" -ib "$interpolate_braced" -di "$deep_interpolate" \
                        -ei "$esc_interpolate" -e "$evaluate" -ee "$esc_evaluate" "$getter" "$evaluator" <<< "$value")
                fi

                output+="$value"
                rest="${rest:$len}"
                continue
            fi
        fi

        if [[ "$evaluate" == true ]];then
            match=$(token_match "$rest" "$_EVALUATE_START_PATTERN")
            if [[ -n "$match" ]]; then
                match=${match::-1}
                len=${#match}

                [[ "$esc_evaluate" == true ]] && match="${match:0:$((len/2))}"

                output+="$match"

                rest="${rest:$len}"

                # Even number of backslashes → evaluate
                if (( len % 2 == 0 )); then
                    local value
                    value=$(evaluate_string inner_evaluator "$rest")
                    local eval_len=${#value}
                    value=$("$evaluator" "$getter" "${value:1:-1}")

                    local status=$?

                    if (( status == 0 )); then
                        output+="$value"
                        rest="${rest:$eval_len}"
                        continue
                    fi
                fi
                output+="{"
                rest="${rest:1}"
                continue
            fi
        fi

        match=$(token_match "$rest" "$_OTHER_PATTERN")
        if [[ -n "$match" ]]; then
            local len=${#match}
            output+="$match"
            rest="${rest:$len}"
            continue
        fi

        output+="${rest:0:1}"
        rest="${rest:1}"
        done

    echo "$output"
}

function bash_c() {
    local getter=${1:-bash_env}  # function name
    local value=$2
    local result

    # Inject the function definition dynamically into the subshell
    bash -c "$(declare -f "$getter"); set -e; $value"
}

evaluate_string() {
    local evaluator=${1:-bash_c}
    local getter=${2:-bash_env}
    local source
    local output=""
    local rest
    depth=0

    source=$(src "${2:-}")
    rest=$source

    while [[ -n "$rest" ]]; do
        local match
        # Single-quoted string
        match=$(token_match "$rest" "$_SINGLE_QUOTED_STRING_PATTERN")
        if [[ -n "$match" ]]; then
            output+="$match"
            rest="${rest:${#match}}"
            continue
        fi

        # Double-quoted string
        match=$(token_match "$rest" "$_DOUBLE_QUOTED_STRING_PATTERN")
        if [[ -n "$match" ]]; then
            output+="$match"
            rest="${rest:${#match}}"
            continue
        fi

        # Opening brace
        if [[ "${rest:0:1}" == "{" ]]; then
            ((depth++))
            output+="{"
            rest="${rest:1}"
            continue
        fi

        # Closing brace
        if [[ "${rest:0:1}" == "}" ]]; then
            ((depth--))
            output+="}"
            if (( depth == 0 )); then
                "$evaluator" "$getter" "$output"
                return
            fi
            rest="${rest:1}"
            continue
        fi

        # Default: any char
        output+="${rest:0:1}"
        rest="${rest:1}"
    done

    # Print output if no outer braces
    echo "$output"
}

declare -A vars=(
    [test]='Hello'
    [greet]='${test}, world!'
    [nested]='${greet}!!!'
    [nested.10.o]='Resolved'
    [key]='Ke'
    [func]='Func'
)

# get_value receives a name reference to an array
function getter() {
    local -n keys=$1   # keys is now a reference to the array
    local key_path
    key_path=$(IFS='.'; echo "${keys[*]}")  # join keys with dots
    echo "Key:$keys">&2
    echo "${vars[$key_path]}"
}

v=$(printf '%s\n' 'declare -A p=("func")' 'getter p')

example=$(cat <<'EOF'
Some khkjlj \\\\\\{getter A} $${echo lkl} $nested
EOF
)
# Example test
examples=(
 "$example"
)

for ex in "${examples[@]}"; do
    echo "Input:  $ex"
    result=$(substitute_string -i true getter "" "$ex")
    echo "Output: $result"
    echo "-----------------------"
done
