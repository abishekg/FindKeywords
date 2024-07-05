## Hello!

### I am [Abishek Goutham](https://www.abishekgoutham.com)

This is a small project which finds the keywords present in a file based on the words present in another.

Language: Java

### How to run the program

1. Create a `predefinedWords.txt` with the list of words you want to search. (Ensure you place them one below the other
   with no blank lines).
2. Create an `input.txt` which has multiple lines of code. This could be of any size. (Over 20MB to 200MB).
3. Replace the two new files in the directory [src/main/resources](src/main/resources) with the existing files. (
   Ensure the files are named correctly).
4. In your terminal, navigate to the path where this repository is cloned.
5. Run command `./gradlew clean build`. (This cleans the previously installed bundle files and builds the gradle again).
6. Run command `./gradlew run`. (This executes the code).

### What has been tested

1. I start by reading through the words in `predefinedWords.txt` and save them to a HashMap `predefinedWordsMap`. I
   ensure I convert them to lowercase to check them against other words easily and also maintain the format of the word
   as the value in my map.
2. I then read through the file `input.txt` line by line and compare each word against the words present in
   the `predefinedWordsMap` and ensure all these checks are done irrespective of its case.
3. I am checking if there are predefinedWords with spaces, I ensure those words get tested accordingly. For
   eg: `Ruby on Rails` exists in the file `predefinedWords.txt`. So I ensure I count the number instances of each
   occurrence of "Ruby on Rails" irrespective of the case.
4. Also ensure that the order and the format of each word in `predefinedWords.txt` is maintained during the output.
5. I am creating an instance of time before I start reading and after I print to determine the total time taken.
6. Initially, when I had only around 45-50 words in the `predefinedWords.txt` file, I was able to run and it only took
   about 5 seconds and I thought I was done with the implementation and I could submit. But when I increased the number
   of words to roughly 10000, I could see it was taking forever. I realized I had to implement multithreading in this in
   order to run them concurrently, and that's where the real challenge started in order to determine how to break down
   the tasks efficiently before I could start with the implementation, but I was glad when it only took roughly 2 and
   half minutes for 10,000 words of length 5.

Thank you so much for this opportunity. The task looked simple in the first go but every iteration through the code over
and over made me think stuff from a different perspective. 
