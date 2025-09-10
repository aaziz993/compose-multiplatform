#!/usr/bin/env bash
# shellcheck disable=SC2016

. "$(dirname "$(readlink -f "$0")")/../scripts/utils.sh"

_EVEN_DOLLARS_REGEX='(?:\$\$)+'
_INTEGER='\d+'
_DOUBLE_QUOTED_STRING_REGEX='"(?:[^"\\]*(?:\\["nrtb\\][^"\\]*)*)"'
_ID_REGEX='[_\p{L}][_\p{L}\p{N}]*'
_INTERPOLATE_REGEX='\$('$_ID_REGEX')'
_KEY_REGEX='(?:'"$_ID_REGEX"'|'"$_INTEGER"'|'"$_DOUBLE_QUOTED_STRING_REGEX"')'  # non-capturing
_INTERPOLATE_BRACED_REGEX='\$\{\s*('"$_KEY_REGEX"'(?:\s*\.\s*'"$_KEY_REGEX"')*)\s*\}'  # capture full key path
_EVALUATE_REGEX='(\\*\{)'
_OTHER_REGEX='[^$]+'

function get_env() {
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
    local get_value
    local source
    local output=""
    local rest

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

    get_value=${1:-get_env}
    source=$(src "${2:-}")
    rest=$source

    # Process string
    while [[ -n "$rest" ]]; do
        local match
        if [[ "$interpolate" == true || "$interpolate_braced" == true ]]; then
            match=$(perl -e 'my ($s,$regex) = @ARGV; if($s=~/^($regex)/){print $&}' "$rest" "$_EVEN_DOLLARS_REGEX")
            if [[ -n "$match" ]]; then
                local len=${#match}
                [[ "$esc_interpolate" == true ]] && match=$(printf "%*s" "$((len/2))" "" | tr " " "$")
                output+="$match"
                rest="${rest:$len}"
                continue
            fi
        fi

        if [[ "$interpolate" == true ]]; then
            match=$(perl -e 'my ($s) = @ARGV; if($s=~/^\$(\w+)/){print $1}' "$rest")
            if [[ -n "$match" ]]; then
                local -a path=("$match")
                local value
                value=$(get_value path)
                local status=$?
                if [[ $status == 0 && "$deep_interpolate" == true ]]; then
                    value=$(substitute_string -i "$interpolate" -ib "$interpolate_braced" -di "$deep_interpolate" \
                        -ei "$esc_interpolate" -e "$evaluate" -ee "$esc_evaluate" get_value <<< "$value")
                fi

                output+="$value"
                rest="${rest:${#match}+1}"
                continue
            fi
        fi

        if [[ "$interpolate_braced" == true ]]; then
            match=$(perl -MJSON -CS -e '
                my ($s,$regex,$kr) = @ARGV;
                if ($s =~ /^($regex)/) {
                    my $full = $1;
                    my $len = length($&);
                    my @keys;
                    while ($full =~ /$kr/g) {
                        my $key = $&;
                        $key =~ s/^"(.*)"$/$1/;
                        push @keys, $key;
                    }
                    print "$len\n" . encode_json(\@keys)
                }
            ' "$rest" "$_INTERPOLATE_BRACED_REGEX" "$_KEY_REGEX")

            if [[ -n "$match" ]]; then
                local len="${match%%$'\n'*}"
                mapfile -t path < <(echo "${match#*$'\n'}" | jq -r '.[]')
                local value
                value=$($get_value path)

                if [[ "$deep_interpolate" == true ]]; then
                    value=$(substitute_string -i "$interpolate" -ib "$interpolate_braced" -di "$deep_interpolate" \
                        -ei "$esc_interpolate" -e "$evaluate" -ee "$esc_evaluate" get_value <<< "$value")
                fi

                output+="$value"
                rest="${rest:$len}"
                continue
            fi
        fi

        if [[ "$evaluate" == true ]];then
            match=$(perl -e 'my ($s,$regex)=@ARGV; if ($s =~ /^$regex/){print $1}' "$rest" "$_EVALUATE_REGEX")
            if [[ -n "$match" ]]; then
                local len=${#match}
                [[ "$esc_evaluate" == true ]] && match=$(printf "%*s" "$(((len-1)/2))" "" | tr " " "\\")
                output+="$match"

                if (( len % 2 == 0 )); then
                    output+="{"
                fi
                rest="${rest:$len}"
                continue
            fi
        fi

        match=$(perl -e 'my ($s,$regex)=@ARGV; if ($s =~ /^($regex)/){print $&}' "$rest" "$_OTHER_REGEX")
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

declare -A vars=(
    [test]='Hello'
    [greet]='${test}, world!'
    [nested]='${greet}!!!'
    [nested.10.o]='Resolved'
    [key]='Ke'
)

# get_value receives a name reference to an array
function get_value() {
    local -n keys=$1   # keys is now a reference to the array
    local key_path
    key_path=$(IFS='.'; echo "${keys[*]}")  # join keys with dots
    echo "${vars[$key_path]}"
}

val=$(cat <<EOF
Nested: \${ nested. 10 .o } \${nested} and simple: \$\$\${test} \$key \$\$\$\$\$ \\\\{}
EOF
)

substitute_string -i true -ib true -di true -ei true -ee false get_value "$val"
