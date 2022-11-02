for file in "chapter18"/*.java
	do
		mv -- "$file" "${file%.java}.txt"
	done