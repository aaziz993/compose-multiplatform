#!/usr/bin/env bash
# shellcheck disable=SC2016

. "$(dirname "$(readlink -f "$0")")/../scripts/utils.sh"

function halve() {
    local value=$1
    local half_len=$((${#value} / 2))
    printf "%s" "${value:0:half_len}"
}

_SINGLE_QUOTED_STRING_PATTERN="'[^\\']*(\\['nrtb\\][^\\']*)*'"
_DOUBLE_QUOTED_STRING_PATTERN='"[^\\"]*(\\["nrtb\\][^\\"]*)*"'
_ID_PATTERN='[_\p{L}][_\p{L}\p{N}]*'
_INTERPOLATE_PATTERN='(\$+)('$_ID_PATTERN')'
_KEY_PATTERN='(?:'"$_ID_PATTERN"'|\d+|'"$_DOUBLE_QUOTED_STRING_PATTERN"')'
_INTERPOLATE_BRACED_PATTERN='(\$+)\{(\s*'"$_KEY_PATTERN"'(?:\s*\.\s*'"$_KEY_PATTERN"')*\s*)\}'
_EVEN_DOLLARS_PATTERN='(?:\$\$)+'
_EVALUATE_START_PATTERN='(\\*)\{'
_OTHER_PATTERN='[^$\\{]+'

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

        if [[ "$interpolate" == true ]]; then
            read -r dollars key <<< "$(perl -e 'my ($s, $regex) = @ARGV; if($s =~ /^$regex/) { print "$1 $2"; }' \
                "$rest" "$_INTERPOLATE_PATTERN")"

            if [[ -n "$dollars" ]]; then
                len=${#dollars}
                local value=$key

                if (( len % 2 != 0 )); then
                    local -a path=("$key")
                    dollars=${dollars:1}

                    value=$("$getter" path)

                    local status=$?
                    if [[ $status == 0 && "$deep_interpolate" == true ]]; then
                        value=$(substitute_string -i "$interpolate" -ib "$interpolate_braced" -di "$deep_interpolate" \
                            -ei "$esc_interpolate" -e "$evaluate" -ee "$esc_evaluate" "$getter" "$evaluator" <<< "$value")
                    fi
                fi

                [[ "$esc_interpolate" == true ]] && dollars=$(halve "$dollars")

                output+="$dollars$value"
                rest="${rest:$len+${#key}}"
                continue
            fi
        fi

        if [[ "$interpolate_braced" == true ]]; then
            read -r dollars raw_path <<< "$(perl -e 'my ($s, $regex) = @ARGV; if($s =~ /^$regex/) { print "length(&)$1 $2"; }' \
                            "$rest" "$_INTERPOLATE_BRACED_PATTERN")"

            if [[ -n "$dollars" ]]; then
                len=${#dollars}
                local value=$raw_path

                if (( len % 2 != 0 )); then
                    dollars=${dollars:1}
                    mapfile -t path < <(perl -MJSON -CS -e '
                                        my ($s, $regex) = @ARGV;
                                        my @keys;
                                        while ($s =~ /$regex/g) {
                                            my $key = $&;
                                            $key =~ s/^"(.*)"$/$1/;
                                            push @keys, $key;
                                        }
                                        print encode_json(\@keys);
                                    ' "$raw_path" "$_KEY_PATTERN")

                    value=$("$getter" path)
                    if [[ "$deep_interpolate" == true ]]; then
                        value=$(substitute_string -i "$interpolate" -ib "$interpolate_braced" -di "$deep_interpolate" \
                            -ei "$esc_interpolate" -e "$evaluate" -ee "$esc_evaluate" "$getter" "$evaluator" <<< "$value")
                    fi
                fi

                [[ "$esc_interpolate" == true ]] && dollars=$(halve "$dollars")
echo $output>&2
                output+="$dollars$value"
                rest="${rest:$len+${#row_path}+2}"
                continue
            fi
        fi

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
    echo "Key:$key_path">&2
    echo "${vars[$key_path]}"
}

v=$(printf '%s\n' 'declare -A p=("func")' 'getter p')

example=$(cat <<'EOF'
Some khkjlj \\\\\\{getter A} $${echo lkl} $nested
EOF
)
# Example test
examples=(
 "\$\$key \$\$\$ \${nested}"
)

for ex in "${examples[@]}"; do
    echo "Input:  $ex"
    result=$(substitute_string -i true -ei true getter "" "$ex")
    echo "Output: $result"
    echo "-----------------------"
done
