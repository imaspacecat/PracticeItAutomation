for chapter in chapter*
do
	for file in "$chapter"/*.java 
	do 
		mv -- "$file" "${file%.java}.txt" 
	done
done
