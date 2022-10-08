for chapter in chapter*
do
	for file in "$chapter"/*.java 
	do 
		mv -- "$file" "${file%.java}.txt" 
	done
done

for file in "graphics"/*.java
do
  mv -- "$file" "${file%.java}.txt"
done

