Hello!

I am [Abishek Goutham](https://www.abishekgoutham.com)

This is a small project which finds the keywords present in a file based on the words present in another.

To run this project, you need to follow the below steps:

1. Create a `predefinedWords.txt` with the list of words you want to search. (Ensure you place them one below the other
   with no blank lines).
2. Create an `input.txt` which has multiple lines of code. This could be of any size. (Over 20MB to 200MB).
3. Replace the two new files in the directory [src/main/resources](src/main/resources) with the existing files. (
   Ensure the files are named correctly).
4. In your terminal, navigate to the path where this repository is cloned.
5. Run command `./gradlew clean build`. (This cleans the previously installed bundle files and builds the gradle again).
6. Run command `./gradlew run`. (This executes the code).
