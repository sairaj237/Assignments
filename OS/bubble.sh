bubble_sort() {
local array=("$@")
local n=${#array[@]}

for((i=0;i<n;i++));do
for((j=0;j<n-i-1;j++));do
if((array[j]>array[j+1]));then
	temp=${array[j]}
	array[j]=${array[ j + 1 ]}
	array[j+1]=$temp
	fi
done
done

echo "sorted array:"
echo "${array[@]}"
}

input_array=(64 24 15 12 25 58)
echo "original array: ${input_array[@]}"

bubble_sort "${input_array[@]}"



