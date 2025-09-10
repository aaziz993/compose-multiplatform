#!/usr/bin/env bash
# shellcheck disable=SC2016

. "$(dirname "$(readlink -f "$0")")/../scripts/utils.sh"

_EVEN_DOLLARS_REGEX='(\$\$)+'
_REFERENCE_REGEX='\${(\w+(?:\.\w+)?)}'
_EVALUATE_REGEX='(\\*\{)'
_OTHER_REGEX='[^$]'

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
    local interpolate=true
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

        if [[ "$interpolate" == true ]]; then
            match=$(perl -e 'my ($s,$regex)=@ARGV; if($s=~/^($regex)/){print $1}' "$rest" "$_EVEN_DOLLARS_REGEX")
            if [[ -n "$match" ]]; then
                local len=${#match}
                [[ "$esc_interpolate" == true ]] && match=$(printf "%*s" "$((len/2))" "" | tr " " "$")
                output+="$match"
                rest="${rest:$len}"
                continue
            fi

            match=$(perl -e 'my ($s,$regex)=@ARGV; if ($s =~ /^($regex)/){print $1}' "$rest" "$_REFERENCE_REGEX")
            if [[ -n "$match" ]]; then
                local path="${match:2:-1}"
                local value
                value=$(get_value "$path")
                local status=$?
                if [[ $status == 0 && "$deep_interpolate" == "true" ]]; then
                  value=$(substitute_string -i "$interpolate" -di "$deep_interpolate" -ei "$esc_interpolate" -e "$evaluate" -ee "$esc_evaluate" get_value <<< "$value")
                fi

                output+="$value"
                rest="${rest:${#match}}"
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

        match=$(perl -e 'my ($s,$regex)=@ARGV; if ($s =~ /^($regex)/){print $1}' "$rest" "$_OTHER_REGEX")
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
    [nested.o]='${greet}!!!'
)

function get_value(){
    local key="$1"
    echo "${vars[$key]}"
}

val=$(cat <<EOF
Nested: \${nested.o} and simple: \$\$\${test} \$\$\$\$\$ \\\\{}
EOF
)

substitute_string -di false -ei false -ee false get_value "$val"
