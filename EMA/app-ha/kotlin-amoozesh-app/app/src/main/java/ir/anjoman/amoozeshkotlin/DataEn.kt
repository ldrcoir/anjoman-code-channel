package ir.anjoman.amoozeshkotlin

/*
 * ترجمهٔ انگلیسی محتوای اپ — ساخته‌شده از منبع فارسی
 * ترتیب با DarsHa.darsList و DarsHa.tamrinHa هم‌تراز است؛
 * هر جا کم‌وکسر شد، Tarjomeh خودش به فارسی برمی‌گردد.
 */

object TarjomehEn {

    val darsHa: List<DarsT> = listOf(
        DarsT(
            "Introduction: What is Programming?",
            listOf(
            "Programming means talking to a computer; but not with human language, but with step-by-step instructions that the computer executes one by one. Like when your mom says: 'Take the tray, arrange the glasses, then pour tea' — if the computer follows the instructions in order, it will do exactly that; without excuses, without fatigue, and infinitely many times.",
            "They call a sequence of ordered instructions that accomplish a goal an 'algorithm'. For example, the algorithm for making tea: 1) Fill the kettle 2) Put it on the stove 3) Wait until it boils 4) Pour the tea 5) Let it steep for three minutes. If you mix up the order — like pouring tea first and then boiling water — the result will be ruined! Programming is the same: order and precision is everything.",
            "Computers only understand the language of zero and one; but nobody talks with zeros and ones! That's why programming languages were created: ways to write with words close to English and have the computer translate them. Python for learning and data, JavaScript for web pages, and Kotlin for building Android apps — each language has its specialty.",
            "Kotlin was created by JetBrains in 2011 and since 2017 it's the official language for Android app development; meaning the same language that apps like Telegram, Bale, and Namava are partially built with. If you want to build an app, Kotlin is your first stop — and luckily it's one of the most user-friendly languages.",
            "Being a good programmer means the ability to break down a big problem into small pieces. 'Building a game' seems strange, but when you break it down to 'showing a question, reading the answer, checking if it's correct, giving points', each piece becomes doable. They call this skill 'programming thinking' and we'll practice it in this course.",
            "A big secret: no programmer writes the correct program from the beginning! Errors (bugs) are part of the job; the art is to read the error message, understand where the problem is, and fix it. This app also has a playground for you that explains errors in Persian with line numbers; so don't be afraid of making mistakes, every error is a step forward."
            ),
            listOf(
            SoalT(
                "In the simplest definition, what does programming mean?",
                listOf(
            "Giving step-by-step instructions to a computer",
            "Changing the computer's Windows",
            "Building hardware",
            "Typing fast"
                ),
                "Exactly right! A program means step-by-step instructions that a computer executes.",
                "Programming means writing step-by-step instructions for a computer."
            ),
            SoalT(
                "What is an algorithm?",
                listOf(
            "A piece of hardware",
            "A sequence of ordered instructions to reach a goal",
            "A type of computer virus",
            "The name of a programming language"
                ),
                "Bravo! An algorithm is like a recipe; ordered and precise.",
                "An algorithm means a sequence of ordered instructions to reach a goal; like a food recipe."
            ),
            SoalT(
                "Which company created Kotlin?",
                listOf(
            "Google",
            "Apple",
            "JetBrains",
            "Microsoft"
                ),
                "Correct! JetBrains created it and then Google made it official.",
                "JetBrains company created Kotlin in 2011."
            ),
            SoalT(
                "Since which year is Kotlin the official language for Android app development?",
                listOf(
            "2011",
            "2017",
            "2020",
            "2015"
                ),
                "Bravo! Google made it official in 2017.",
                "Since 2017, Kotlin is the official language for Android."
            ),
            SoalT(
                "What language does a computer ultimately understand?",
                listOf(
            "English",
            "Persian",
            "Kotlin",
            "Zero and one"
                ),
                "Exactly right! At the lowest level, everything is zero and one.",
                "Computers only understand zero and one; languages are the translators in between."
            ),
            SoalT(
                "What is a Bug?",
                listOf(
            "A type of hardware",
            "An error in a program that must be found and fixed",
            "The name of comments",
            "Processing speed"
                ),
                "Correct! A bug means a program error; and fixing bugs is part of a programmer's life.",
                "A bug means a program error; all programmers have bugs and fix them."
            )
            ),
            listOf(
            "                \"The algorithm 'Blocking an Orange' in programming language",
            "                \"Same instructions, out of order = disaster",
            "                \"Programming thinking: breaking down big problems"
            )
        ),

        DarsT(
            "App Guide: How to Learn Properly",
            listOf(
            "The learning path in this app is a complete cycle: first you read the \"lesson\" and see its code examples, then you take the \"quiz\" for that lesson to make sure you understood correctly, and finally you do the \"practical exercise\" where you write code yourself. This cycle repeats for each lesson; skipping any of the loops means you're missing half of the learning.",
            "\"Code Playground\" is the heart of this app; a real Kotlin execution environment right inside your phone, without a computer and without internet. Write code, press the run button, and see the output. If your code has errors, the app will tell you exactly which line and what problem — in Persian with examples. This is where you should make mistakes; no one is watching!",
            "In the \"Practical Exercises\" section, each exercise has an incomplete code and a target output. Make the program output exactly that. If you get stuck: first re-read the related lesson, then look at its code examples. Getting stuck is natural; even professional programmers get stuck on a single line for hours.",
            "Each lesson quiz gives a score out of 20 and saves your best score; each question immediately tells you whether you got it right and why. From the app menu, you can take a \"Comprehensive Quiz\" that pulls questions from all lessons — a real measure to see if you're ready for the next lesson.",
            "Open the \"Theme & Appearance\" section from the menu (☰): you have six color themes; choose any you like; the entire app changes instantly. Learning with colors you enjoy is more fun!",
            "Three golden tips: First — a little every day is better than a lot in one day; the brain learns in between. Second — type, don't copy; finger muscles are also partners in learning. Third — don't stare stubbornly at the screen; ask questions, ask your mentor, ask your friend."
            ),
            listOf(
            SoalT(
                "What is the correct learning cycle in this app?",
                listOf(
            "Exercise ← Lesson ← Quiz",
            "Lesson ← Quiz ← Practical Exercise",
            "Quiz ← Exercise ← Lesson",
            "Just exercise is enough"
                ),
                "Exactly! First lesson, then quiz, finally practical exercise.",
                "The correct order: read the lesson, take the quiz, then do the exercise."
            ),
            SoalT(
                "What is the Code Playground for?",
                listOf(
            "Just for fun",
            "Writing and running real code inside the app, with Persian error reporting",
            "Just reading code without execution",
            "Chatting with friends"
                ),
                "Bravo! Playground means real code execution with Persian error messages.",
                "Playground is where you write and run real code; errors are also explained in Persian."
            ),
            SoalT(
                "If you get stuck on an exercise, what's the best thing to do?",
                listOf(
            "Delete the app",
            "Sit up all night without paying attention",
            "Re-read the related lesson and look at the code examples",
            "Jump to harder exercises"
                ),
                "Correct! Reviewing the lesson and examples is the first way out.",
                "Stuck? Re-read the lesson, look at the code examples, ask questions."
            ),
            SoalT(
                "What's the difference between the Comprehensive Quiz and regular lesson quizzes?",
                listOf(
            "Its score is double",
            "It pulls questions from all lessons",
            "It only has hard questions",
            "It has a time limit and you must answer quickly"
                ),
                "Bravo! The Comprehensive Quiz pulls questions from all lessons; a real test of you.",
                "The Comprehensive Quiz collects questions from all lessons."
            ),
            SoalT(
                "Where can you change the app's theme (colors and appearance)?",
                listOf(
            "On the lesson pages",
            "From the ☰ menu in Theme & Appearance section",
            "In the Code Playground",
            "There is no theme change option"
                ),
                "Exactly! From the ☰ menu, \"Theme & Appearance\" section — you have six themes.",
                "Themes can be selected from the ☰ menu and \"Theme & Appearance\" section."
            ),
            SoalT(
                "Which one was NOT one of the golden learning tips in this lesson?",
                listOf(
            "Practice a little every day",
            "Type the code, don't copy",
            "Just watch and learn",
            "Ask questions"
                ),
                "Correct! Just watching doesn't teach; type and practice.",
                "The tips were: a little every day, type, ask questions. Just looking isn't enough."
            )
            ),
            listOf(
            "                \"Try it yourself: Run this code in the Code Playground",
            "                \"Make mistakes, don't be afraid! See what the program says"
            )
        ),

        DarsT(
            "What is Kotlin and Your First Program",
            listOf(
            "Kotlin is the official programming language for making Android apps; it's the same language that Google uses to build apps like Telegram, Bala, and Instagram. Kotlin was created by JetBrains in 2011 and became the official Android language in 2017.",
            "Every Kotlin program starts at a specific point: a function called main. Any code you write inside the curly braces of fun main() will run from top to bottom, in order. Like the start key of a car; without main, the program won't start at all.",
            "We use println to display text or numbers. println prints whatever you put inside the parentheses and then moves to the next line. If you don't want to go to the next line, use print without the \"ln\". Write the letters in lowercase; println with a capital P will cause an error!",
            "You can also show the values of variables inside text: just put a dollar sign (\$) before the variable name; like \"Hello \$name\". Whenever you want to leave a note for yourself, start the line with two slashes //; comments don't get executed at all and are just for making the code readable."
            ),
            listOf(
            SoalT(
                "Where does a Kotlin program start?",
                listOf(
            "From the first line of the file",
            "From fun main()",
            "From fun start()",
            "From println"
                ),
                "Exactly! The starting point of all Kotlin programs is fun main().",
                "The starting point of the program is always fun main(); the start key of the program!"
            ),
            SoalT(
                "What's the difference between println and print?",
                listOf(
            "println goes to the next line after printing",
            "println is only for numbers",
            "print runs faster",
            "There's no difference"
                ),
                "Great! ln stands for line; meaning \"then go to the next line\".",
                "println goes to the next line after printing; print stays in the same place."
            ),
            SoalT(
                "What is the output of this code?\nval name = \"Sarah\"\nprintln(\"Hello \$name\")",
                listOf(
            "Hello \$name",
            "Hello Sarah",
            "Hello",
            "It will cause an error"
                ),
                "Exactly! The dollar sign puts the variable's value in the middle of the text.",
                "The \$ sign before the variable name puts its value inside the text; so it prints: Hello Sarah"
            ),
            SoalT(
                "Which one is a comment (note) and doesn't get executed at all?",
                listOf(
            "// This is a note",
            "print(\"note\")",
            "** note **",
            "<!-- note -->"
                ),
                "Correct! In Kotlin, comments start with two slashes //.",
                "In Kotlin, comments start with //; # is for Python and <!-- --> is for HTML."
            ),
            SoalT(
                "What happens if we write println with a capital P (Println)?",
                listOf(
            "It does the same thing",
            "It prints in large letters",
            "We get an error",
            "Something gets deleted"
                ),
                "Exactly! Kotlin is case-sensitive.",
                "Kotlin is case-sensitive; function names must be exactly println."
            ),
            SoalT(
                "In what order are the commands in the main function executed?",
                listOf(
            "From the last line to the first",
            "In order from top to bottom",
            "Randomly",
            "All at once"
                ),
                "Great! Code is read like a book: line by line from top to bottom.",
                "Kotlin executes commands in order from top to bottom."
            ),
            SoalT(
                "Why is programming \"like a recipe\"?",
                listOf(
            "Because both are tasty",
            "Because both are sequences of ordered commands with a specific result",
            "Because both are done in the kitchen",
            "They have nothing in common"
                ),
                "Exactly! Order and precision are vital in both.",
                "All programs are like recipes; they're sequences of ordered commands; messing up the order means ruining the result."
            ),
            SoalT(
                "To see the result of a small code in this app, where should you go?",
                listOf(
            "Code playground",
            "About page",
            "Theme menu",
            "Test page"
                ),
                "Great! The playground is where you write code, run it, and see the output.",
                "The code playground is where you write code, run it, and see the output."
            )
            ),
            listOf(
            "                \"Little Report Card Program",
            "                \"Comments: Programmer's Notes"
            )
        ),

        DarsT(
            "Variables: val, var and Data Types",
            listOf(
            "A variable is like a labeled box where we put a value to use later. In Kotlin, we have two ways to make boxes: val for sealed boxes that don't change after being filled, and var for boxes that you can change the contents of whenever you want.",
            "The golden rule of professional programmers: use val whenever you can! Because when a value is locked, nothing can break it and the program becomes safer. Only use var for things that really need to change (like game scores).",
            "Every variable has a type (data type): Int for whole numbers like 17, Double for decimal numbers like 19.5, String for text, and Boolean for true/false logic (only true or false). The nice thing about Kotlin is that it automatically figures out the type from the value; this is called type inference.",
            "If you want to specify the type yourself, write a colon and the type after the variable name: val age: Int = 17. Where is this used? When you want to tell the code reader that this variable must be a certain type, or when you want Kotlin to catch your mistake sooner; for example, if you put a decimal number in an Int, you get an error right away."
            ),
            listOf(
            SoalT(
                "Which variable can be changed later?",
                listOf(
            "val score = 10",
            "var score = 10",
            "None",
            "Both"
                ),
                "Correct! var means variable, meaning changeable.",
                "Only var is changeable; val stays like a lock."
            ),
            SoalT(
                "What is the data type of \"Kotlin\"?",
                listOf(
            "Int",
            "String",
            "Double",
            "Boolean"
                ),
                "Yes! Any text between quotation marks is a String.",
                "Texts are of type String; they sit between two quotation marks."
            ),
            SoalT(
                "val grade = 19.5 is of what type?",
                listOf(
            "Int",
            "Boolean",
            "Double",
            "String"
                ),
                "Exactly! Decimal numbers are Double.",
                "Decimal numbers (with a dot) are of type Double."
            ),
            SoalT(
                "What values can be put in a Boolean?",
                listOf(
            "Positive and negative numbers",
            "true and false",
            "Any text",
            "Zero and one as text"
                ),
                "Great! Boolean only has two states: true or false.",
                "Boolean only accepts true (correct) or false (incorrect)."
            ),
            SoalT(
                "Why do programmers say \"use val whenever you can\"?",
                listOf(
            "Because val runs faster",
            "Because val uses less memory",
            "Because a locked value can't be broken and the program is safer",
            "Because val starts with val"
                ),
                "Exactly! Fixed value = fewer bugs. This is a professional habit.",
                "The advantage of val is safety: once locked, no line of code can change it intentionally or by mistake."
            ),
            SoalT(
                "val age: Int = 17 — what is the role of \": Int\" in this line?",
                listOf(
            "Making a comment",
            "Manually specifying the variable type",
            "Adding two numbers",
            "Printing a number"
                ),
                "Yes! A colon and type means \"this box only accepts whole numbers\".",
                "After the colon, the variable type is written; if the value doesn't match, we get an error."
            ),
            SoalT(
                "What is the output of this code?\nvar score = 10\nscore = score + 5\nprintln(score.toString())",
                listOf(
            "10",
            "15",
            "Error; val cannot be changed",
            "5"
                ),
                "Great! var is changeable; 10+5=15.",
                "var means changeable; the new value becomes 15."
            ),
            SoalT(
                "If you write val x = 5 and then x = 8, what happens?",
                listOf(
            "x becomes 8",
            "It gives an error; val is locked",
            "x gets two values",
            "Nothing"
                ),
                "Correct! val gets locked after the initial value.",
                "val only takes a value once; changing it is an error. Use var for changes."
            )
            ),
            listOf(
            "                \"Profile Boxes",
            "                \"Type Conversion: Text to Number and Backwards"
            )
        ),

        DarsT(
            "Operators: Math, Comparison, and Logic",
            listOf(
            "Operators are small symbols that work with values. Mathematical operators are the ones you know from math: + for addition, - for subtraction, * for multiplication, and / for division. There's also a less known operator % that gives the remainder of division: 10 % 3 equals 1, because 3 goes into 10 three times with 1 left over.",
            "Be careful with integer division: in Kotlin, dividing two Ints gives an Int! That means 7 / 2 becomes 3 (not 3.5). If you want a decimal answer, one of the numbers should be decimal: 7 / 2.0 becomes 3.5. This point often creates questions in programming competitions!",
            "Kotlin has nice shortcuts: instead of x = x + 5, you can write x += 5. The same works for -=, *=, and /=. For adding and subtracting one, we have ++ and --: i++ means i becomes one more. These are called \"compound assignment operators\".",
            "Comparison operators check two values and return true or false: == equals, != not equals, < and > less than and greater than, and <= and >= with equality. And logical operators combine conditions: && means \"and\" (both must be true), || means \"or\" (one is enough), and ! means \"not\" (to flip). Example: age >= 12 && age <= 18 means \"from 12 to 18 years old\"."
            ),
            listOf(
            SoalT(
                "What is the result of 10 % 3?",
                listOf(
            "3",
            "1",
            "0",
            "3.33"
                ),
                "Great! The remainder of 10 divided by 3 is 1.",
                "% gives remainder: 10 = 3×3 + 1, so the answer is 1."
            ),
            SoalT(
                "What is the output of 7 / 2 in Kotlin?",
                listOf(
            "3.5",
            "3",
            "4",
            "Error"
                ),
                "Exactly! Dividing two Ints gives an Int; the decimal part is dropped.",
                "Dividing two integers gives an integer result: 7 / 2 = 3. For 3.5, write 7 / 2.0"
            ),
            SoalT(
                "What line is exactly the same as x += 5?",
                listOf(
            "x = 5",
            "x = x + 5",
            "x + 5",
            "x = 5 + 5"
                ),
                "Great! += means \"add to the previous value\".",
                "x += 5 is shorthand for x = x + 5."
            ),
            SoalT(
                "How is the condition \"age between 12 and 18\" written in Kotlin?",
                listOf(
            "age >= 12 && age <= 18",
            "12 <= age <= 18",
            "age > 12 || age < 18",
            "age in 12"
                ),
                "Exactly! Two conditions are joined with &&.",
                "In Kotlin, you can't write 12 <= age <= 18; two separate conditions with && are needed."
            ),
            SoalT(
                "When does the && operator return true?",
                listOf(
            "If one side is true",
            "Only when both sides are true",
            "Never",
            "When both sides are false"
                ),
                "Right! && is strict: both must be true.",
                "&& means \"and\": only when both conditions are true is the result true."
            ),
            SoalT(
                "What is the result of !(5 > 3)?",
                listOf(
            "true",
            "false",
            "5",
            "Error"
                ),
                "Exactly! 5 > 3 is true, and ! flips it to false.",
                "! flips the result: 5 > 3 means true, so !(5 > 3) becomes false."
            ),
            SoalT(
                "What is the result of 7 % 3?",
                listOf(
            "۲",
            "۱",
            "۰",
            "۲٫۳"
                ),
                "Great! The remainder of 7 divided by 3 is 1.",
                "% gives the remainder of division; 7 = 2×3 + 1 so the answer is 1."
            ),
            SoalT(
                "To check if a number is even, which condition is correct?",
                listOf(
            "n % 2 == 0",
            "n / 2 == 0",
            "n == 2",
            "n + 2 == 0"
                ),
                "Exactly! A remainder of 0 when divided by 2 means it's even.",
                "n % 2 == 0 means n is divisible by 2; that means it's even."
            )
            ),
            listOf(
            "                \"Logical Operators: && and ||",
            "                \"Operator Order: Parentheses change everything"
            )
        ),

        DarsT(
            "Strings: Working with Text",
            listOf(
            "A String is a sequence of characters; basically text. Text always goes between two quotation marks \" \". You can join two strings with +: \"code\" + \"writing\" becomes \"codewriting\". And with string templates (\$), you can put variable values inside text; even calculations: \"Sum: \${a + b}\".",
            "Strings have some built-in tools that you connect with a dot: length gives the number of characters, uppercase makes all letters uppercase, lowercase does the opposite, and reversed reverses the string. Example: \"kotlin\".uppercase() becomes \"KOTLIN\" and its length is 6.",
            "Sometimes text you get from a user is actually a number; for example «۲۵». To convert text to a number, we use toInt() and toDouble(): \"25\".toInt() becomes the number 25. But be careful if the text isn't a number (like \"hello\".toInt()), the program will error; because you can't make hello into a number!",
            "Subtle point: \"25\" (text) is different from 25 (number). The first is two characters next to each other, and the second is an actual number you can do math with. You can't do math operations on text; you first need to convert it to a number with toInt(). This difference is the basis of many exercises."
            ),
            listOf(
            SoalT(
                "What is the output of \"code\" + \"writing\"?",
                listOf(
            "code writing",
            "codewriting",
            "Error",
            "code+writing"
                ),
                "Great! + joins two strings without space.",
                "String concatenation means joining; since we didn't put a space, it's printed joined together."
            ),
            SoalT(
                "val s = \"Kotlin\"; what is the output of s.length?",
                listOf(
            "5",
            "6",
            "7",
            "K"
                ),
                "Exactly! The word Kotlin has six letters.",
                "length gives the number of characters; Kotlin has six characters."
            ),
            SoalT(
                "What does \"kotlin\".uppercase() give?",
                listOf(
            "Kotlin",
            "KOTLIN",
            "kotlin",
            "Error"
                ),
                "Right! uppercase makes all letters uppercase.",
                "uppercase makes letters uppercase: KOTLIN."
            ),
            SoalT(
                "What do we write to convert the text \"25\" to a number?",
                listOf(
            "\"25\".toInt()",
            "toInt(\"25\")",
            "\"25\" + 0",
            "number(\"25\")"
                ),
                "Exactly! Conversion functions connect to strings with a dot.",
                "Conversions are string methods: \"25\".toInt() gives the number 25."
            ),
            SoalT(
                "If we write \"hello\".toInt(), what happens?",
                listOf(
            "The text hello is printed",
            "We get the number zero",
            "It gives an error",
            "The string becomes empty"
                ),
                "Great! You can't make hello into a number; the program will error.",
                "If the text can't be converted to a number, the program stops with an error."
            ),
            SoalT(
                "What is the output of \"Sum: \${'\$'}{2 + 3}\"?",
                listOf(
            "Sum: 2 + 3",
            "Sum: 5",
            "Sum: 23",
            "Error"
                ),
                "Exactly! Inside dollar and braces, we can do calculations too.",
                "\${'\$'}{...} calculates first, then puts the result in text: Sum: 5"
            ),
            SoalT(
                "The length of the string «kotlin» is determined with which?",
                listOf(
            "kotlin.size",
            "kotlin.length",
            "length(kotlin)",
            "kotlin.count"
                ),
                "Great! Strings have length; lists have size.",
                "For text, we use .length; .size is for lists."
            ),
            SoalT(
                "What is the output of \"hello\".repeat(2)?",
                listOf(
            "hello hello",
            "hello2",
            "It gives an error",
            "hellohello (joined)"
                ),
                "Exactly! repeat puts repetitions joined together.",
                "repeat repeats without space: hellohello. You have to add the space yourself."
            )
            ),
            listOf(
            "                \"String Templates with \$: Cleaner way to insert",
            "                \"Working with letters: uppercase, lowercase, reverse"
            )
        ),

        DarsT(
            "Getting User Input",
            listOf(
            "So far our programs have been one-way: they only printed things. With readln(), the program gets input from the user: it waits for the user to type text and press Enter, then gives that text to you to put in a variable. Now our programs become two-way; like a conversation!",
            "The output of readln() is always a String; even if the user types a number. So if you need a number, you have to convert it yourself: val age = readln().toInt(). Remember this pattern because it repeats often in real exercises: first input, then conversion, then calculation.",
            "A pro tip: you can chain conversion and input; meaning get input on the same line and convert it to a number. If the user types something other than a number, the program will stop with an error message. That's why before converting, we usually tell the user exactly what to write: «Enter your age as a number».",
            "In the Code Playground for this app, the \"Input\" box at the bottom is for readln: every line you write in it fills a readln; the first line for the first readln, the second line for the second readln, and so on. Remember that the order of the lines is the order of the readln calls."
            ),
            listOf(
            SoalT(
                "What does readln() do?",
                listOf(
            "It gets one line of text from the user",
            "It prints text",
            "It closes the program",
            "It makes a random number"
                ),
                "Great! read means read; ln means one line.",
                "readln() reads one line from user input and gives it as a String."
            ),
            SoalT(
                "What is the output type of readln()?",
                listOf(
            "Int",
            "Double",
            "String",
            "It depends on the user"
                ),
                "Exactly! Whatever the user types, you receive it as text.",
                "readln() always gives a String; even when the user types a number."
            ),
            SoalT(
                "How do we get a number entered by the user?",
                listOf(
            "readln().toInt()",
            "toInt(readln())",
            "readInt()",
            "readln() + 0"
                ),
                "Great! We get input and convert it to a number right away.",
                "The correct pattern: readln().toInt() — meaning read and convert."
            ),
            SoalT(
                "What happens if the user types «eighteen» in response to readln().toInt()?",
                listOf(
            "We get the number 18",
            "We get zero",
            "The program gives an error",
            "It asks the question again"
                ),
                "Exactly! «Eighteen» cannot be converted to a number, so we get an error.",
                "Non-numeric text causes an error during toInt(); that's why we should ask the user for a number."
            ),
            SoalT(
                "In the Playground for this app, where is the second line of the \"Input\" box used?",
                listOf(
            "Second loop iteration",
            "The second readln of the program",
            "The second line of code",
            "Second print"
                ),
                "Yeah! Each input line, in order, fuels a readln.",
                "The lines in the Input box are given to the program's readln in order."
            ),
            SoalT(
                "Why do we use print without ln before readln?",
                listOf(
            "Because print is faster",
            "So the question and user's answer stay on one line",
            "Because println gives an error",
            "It doesn't matter, it's a choice with no effect"
                ),
                "Exactly! «What's your name? » with the user's answer becomes one line; it's cleaner.",
                "print doesn't go to the next line, so the question text stays on the same line as what the user types."
            ),
            SoalT(
                "What does readln return?",
                listOf(
            "Number",
            "Text (String)",
            "List",
            "Boolean"
                ),
                "Great! All input is first text; use toInt to make it a number.",
                "readln always gives text; to get a number, you need to add .toInt()."
            ),
            SoalT(
                "To read a whole number from the user, which line is correct?",
                listOf(
            "val n = readln()",
            "val n = readln().toInt()",
            "val n = toInt(readln())",
            "val n = readln.toInt()"
                ),
                "Exactly! First text is read, then converted to Int.",
                "readln().toInt() means read and convert to a whole number."
            )
            ),
            listOf(
            "                \"Two inputs in a row: adding two user numbers",
            "                \"Hello game with user's name"
            )
        ),

        DarsT(
            "Making Decisions: if and else",
            listOf(
            "Programs, like us, need to make decisions: if it's raining, take an umbrella, otherwise wear sunglasses! In Kotlin, this is done with if: if (condition) means \"if the condition is true, execute the code in the block\". A condition is an expression that results in true or false; like age >= 18 or name == \"Ali\".",
            "If the condition isn't true, we do the else alternative. And if there are more cases, we put else if between them: first the first case is checked, then the second, then the third, and finally if none are true, else is executed. Only one of these blocks runs; the first condition that is true wins.",
            "A cool note: when the body is just one line, braces aren't needed; you can write if (age >= 18) println(\"You're old enough!\"). But when there are two or more lines, braces are required to show where the body ends. Pro tip: always use braces to make your code more readable and less error-prone.",
            "A cool feature: in Kotlin, if is an \"expression\"; meaning it can produce a value itself: val result = if (age > 18) \"Adult\" else \"Teenager\". Here else is mandatory because if the condition is false, you need to know what the answer is. This style of coding is short and clean and is very useful."
            ),
            listOf(
            SoalT(
                "What answer should an if condition give for its body to execute?",
                listOf(
            "Positive number",
            "true",
            "Empty text",
            "Any answer"
                ),
                "Great! if only accepts true.",
                "The body of if only executes when the condition is true."
            ),
            SoalT(
                "If there are several else if in a row, how many blocks execute?",
                listOf(
            "All correct blocks",
            "Only the first correct condition",
            "The last block",
            "None"
                ),
                "Exactly! The first condition that is true executes and the rest are ignored.",
                "The if chain ends with the first correct condition; only that one block executes."
            ),
            SoalT(
                "Which syntax is correct?",
                listOf(
            "if age > 5 { }",
            "if (age > 5) { }",
            "if (age > 5) then { }",
            "when (age > 5) { }"
                ),
                "Yeah! The condition always goes in parentheses.",
                "The if condition must be written in parentheses: if (age > 5)."
            ),
            SoalT(
                "val result = if (age > 18) \"Adult\" else \"Teenager\" — why is else mandatory here?",
                listOf(
            "Because the compiler wants it",
            "Because this if produces a value and both cases must have an answer",
            "Because else is faster",
            "It's not mandatory, it's just style"
                ),
                "Exactly! When if produces a value, the false case must also have an answer.",
                "An if-expression must give a value in both cases (true/false); so else is needed."
            ),
            SoalT(
                "What is the output of this code?\nval n = 10\nif (n > 20) println(\"A\")\nelse if (n > 5) println(\"B\")\nelse println(\"C\")",
                listOf(
            "A",
            "B",
            "C",
            "None"
                ),
                "Great! n > 20 is false, n > 5 is true; so \"B\" is printed.",
                "The first condition is false but the second (n > 5) is true; answer: B"
            ),
            SoalT(
                "When can we skip the braces of an if body?",
                listOf(
            "Always",
            "When the body has only one command",
            "When the condition is simple",
            "Never"
                ),
                "That's right! For one command, you can write it in one line; for more, braces are needed.",
                "A single-command body can be without braces; but the good habit is to always use braces."
            ),
            SoalT(
                "What is the output of this code?\nval age = 20\nif (age >= 18) {\n    println(\"Adult\")\n} else {\n    println(\"Teenager\")\n}",
                listOf(
            "Teenager",
            "Adult",
            "None",
            "Error"
                ),
                "Great! 20 is greater than or equal to 18.",
                "The condition age >= 18 is true; so the if branch executes: Adult."
            ),
            SoalT(
                "When is else if used?",
                listOf(
            "When you want to check several conditions in order and the first true one executes",
            "When no condition is true",
            "Only for numbers",
            "For loops"
                ),
                "Exactly! The if / else if / else chain checks from top to bottom.",
                "else if means \"otherwise if\"; the condition chain executes the first true one and skips the rest."
            )
            ),
            listOf(
            "                \"Nested condition: inside a condition, another condition",
            "                \"Checking a password word"
            )
        ),

        DarsT(
            "when: Cleaner Choice",
            listOf(
            "When you want to compare a value with multiple cases, nested if statements make your code messy. Kotlin has when for this: when (rotbe) { 1 -> ... 2 -> ... else -> ... }. This means 'look at what rotbe is; if it's 1 do this, if it's 2 do that...'. Like a signpost where each path has its own sign.",
            "Each branch is separated with an arrow -> and you can connect multiple values to one branch with commas: 1, 2, 3 -> println(\"سکو\"). For ranges, use in: in 4..10 means 'any number from 4 to 10'. The else branch also covers all untouched cases; like else in if.",
            "when has another power: you can skip the subject entirely and write conditions directly: when { nomre >= 18 -> ... nomre >= 12 -> ... }. Here each branch is a complete condition and the first true one runs; like if and else if but cleaner.",
            "Important note: The order of branches matters! Kotlin checks from top to bottom and stops with the first true branch. So write more specific conditions first and more general ones below. If you leave out else and no branch matches, nothing runs; that's why we usually don't forget else."
            ),
            listOf(
            SoalT(
                "In Kotlin, when is most similar to which statement in other languages?",
                listOf(
            "switch",
            "for",
            "while",
            "return"
                ),
                "Great! when does the same job as switch, but more powerful and cleaner.",
                "when replaces switch in Kotlin."
            ),
            SoalT(
                "What does in 4..10 in a when branch mean?",
                listOf(
            "Print the range 4 to 10",
            "Any value between 4 to 10 will activate this branch",
            "Loop",
            "Error"
                ),
                "Exactly! in with a range means 'within this range'.",
                "in 4..10 checks if the value is between 4 to 10 (including 4 and 10)."
            ),
            SoalT(
                "How do we give three values 1, 2 and 3 to one branch?",
                listOf(
            "1, 2, 3 ->",
            "1 || 2 || 3 ->",
            "in 1..3 only",
            "1 2 3 ->"
                ),
                "Great! You can connect multiple values to one branch with commas.",
                "Separated with commas: 1, 2, 3 -> means any of these will execute this branch."
            ),
            SoalT(
                "When does the else branch in when execute?",
                listOf(
            "Always",
            "When no other branch matches",
            "First",
            "Never executes"
                ),
                "Correct! else is the backup; it only runs when no other case matches.",
                "else is the default case: if no condition or value matches, it runs."
            ),
            SoalT(
                "In when without a subject, which branch executes?",
                listOf(
            "All true branches",
            "First true condition",
            "Last branch",
            "Randomly"
                ),
                "Great! Like if/else if: the first true condition wins.",
                "when without a subject checks conditions from top and stops with the first true one."
            ),
            SoalT(
                "What is the output of this code?\nval n = 7\nwhen (n) {\n    in 1..5 -> println(\"کم\")\n    in 6..10 -> println(\"متوسط\")\n    else -> println(\"زیاد\")\n}",
                listOf(
            "کم",
            "متوسط",
            "زیاد",
            "هیچ‌کدام"
                ),
                "Exactly! 7 is in the range 6 to 10; the second branch runs.",
                "7 is not in 1..5 but is in 6..10; so \"متوسط\" is printed."
            ),
            SoalT(
                "In when, what happens if two conditions are true?",
                listOf(
            "Both branches",
            "Only the first one that was true",
            "Last branch",
            "None"
                ),
                "Great! when is like an elevator: the first correct floor is it.",
                "when executes the first true condition and ignores the rest."
            ),
            SoalT(
                "What role does else at the end of when have?",
                listOf(
            "Repeat the first condition",
            "Any case that doesn't match the conditions",
            "Only zero",
            "End the program"
                ),
                "Correct! else is when's safety net.",
                "else runs when no other condition is true."
            )
            ),
            listOf(
            "                \"Multiple values in one branch: Days of the week",
            "                \"when for texts"
            )
        ),

        DarsT(
            "for Loops and Ranges",
            listOf(
            "Loop means repetition; instead of writing println ten times, you make a loop and Kotlin repeats it ten times. The most common Kotlin loop is for that works on 'ranges': for (i in 1..5) means i becomes 1, 2, 3, 4, and 5 one by one, and the body runs once for each.",
            "Don't forget three important range notations: double dots (..) means from first to last including the last number; 1..3 means 1, 2, and 3. until means up to but not including the last; 0 until 3 means 0, 1, and 2 (often works with list indices). And downTo means counting backward: 5 downTo 1 means 5, 4, 3, 2, and 1.",
            "With step you can change the jump size: for (i in 0..10 step 2) only takes even numbers: 0, 2, 4, 6, 8, and 10. You can combine it with downTo too: 10 downTo 1 step 3 means 10, 7, 4, and 1. Step can't be decimal; it must be whole number (Int).",
            "The variable i defined inside for only lives inside that loop and you don't need to define it beforehand. A classic use: summing numbers; make a var majmoo = 0, inside the loop say majmoo += i, after the loop print it. You'll see this pattern a thousand times in the future!"
            ),
            listOf(
            SoalT(
                "How many times does the for loop (i in 1..5) run?",
                listOf(
            "4 times",
            "5 times",
            "6 times",
            "Infinitely"
                ),
                "Exactly! Double dots count the last number too: 1 to 5 means 5 times.",
                "With .. the last number is included too; so 1, 2, 3, 4, 5: five times."
            ),
            SoalT(
                "What numbers does 0 until 5 take?",
                listOf(
            "From 0 to 5",
            "From 1 to 5",
            "From 0 to 4",
            "Only 0 and 5"
                ),
                "Great! until doesn't take the last number.",
                "until means 'up to but not including'; so 0, 1, 2, 3, and 4."
            ),
            SoalT(
                "What is the output of 5 downTo 1 step 2?",
                listOf(
            "5 4 3 2 1",
            "5 3 1",
            "1 3 5",
            "5 2"
                ),
                "Exactly! Starting from 5, subtract 2 each time until 1: becomes 5, 3, and 1.",
                "downTo counts backward with step 2: 5 and 3 and 1."
            ),
            SoalT(
                "What is the classic pattern for summing numbers inside a loop?",
                listOf(
            "Make a var zero and += inside loop",
            "Println inside loop",
            "Make variable val inside loop",
            "Sum with if"
                ),
                "Great! The sum variable is always defined outside and updated inside the loop.",
                "Correct pattern: var majmoo = 0 before loop and majmoo += i inside loop."
            ),
            SoalT(
                "How many times does for (i in 0..10 step 2) loop?",
                listOf(
            "5 times",
            "6 times",
            "10 times",
            "11 times"
                ),
                "Yeah! Numbers 0, 2, 4, 6, 8, and 10: six numbers, six times.",
                "Step 2 on range 0 to 10: takes 6 numbers so 6 loops."
            ),
            SoalT(
                "Where can a variable defined inside for (like i) be used?",
                listOf(
            "In the whole program",
            "Only inside that loop",
            "Only the line after the loop",
            "Nowhere"
                ),
                "Exactly! i only belongs to that loop and doesn't exist outside.",
                "The loop variable's scope is only the body of that loop."
            ),
            SoalT(
                "What is the output of this code?\nfor (i in 1..3) {\n    print(i.toString())\n}",
                listOf(
            "1 2 3 (with space)",
            "123 (stuck together)",
            "۱۲۳",
            "321"
                ),
                "Exactly! print doesn't go to next line; so it prints stuck together.",
                "print without ln stays on same line; so ۱۲۳ prints stuck together."
            ),
            SoalT(
                "What's the difference between 1..5 and 1 until 5?",
                listOf(
            "No difference",
            "First takes up to 5, second up to 4",
            "First starts from zero",
            "Second counts backward"
                ),
                "Great! until doesn't count the last one.",
                ".. includes the end; until doesn't take the last: 1 until 5 means 1 to 4."
            )
            ),
            listOf(
            "                \"Loop with step: jumping two by two",
            "                \"Loop on list of names"
            )
        ),

        DarsT(
            "While Loops and Loop Control",
            listOf(
            "while is for when you don't know exactly how many times you need to repeat. Its structure is simple: while (condition) { body }; it keeps going as long as the condition is true. Example: «while the game isn't over, read the next move». Since you don't know how many moves there are beforehand, while is the right choice.",
            "Remember to make the condition false somewhere in the body! If the condition never becomes false, the loop becomes infinite and the program gets stuck. The correct pattern: define a var variable (for example, var t = 3), check it in the condition (t > 0), and change it inside the body (t--). Remember this trio: start, condition, change.",
            "You also have two control words: break means «jump out of the loop» and the loop ends immediately; continue means «skip this round and go to the next». For example, in a list of grades, when you see a grade of 20, break (you don't need to continue) and skip negative grades with continue.",
            "In the playground app if you create an infinite loop, after a while you'll get a message: «Program took too long! You probably have an infinite loop». This safety feature prevents the program from getting stuck. In real Kotlin too, if a loop keeps going and the condition never becomes false, the app will crash; so always make sure you have an exit condition."
            ),
            listOf(
            SoalT(
                "How long does a while loop run?",
                listOf(
            "Until 10 times",
            "As long as its condition is true",
            "Only once",
            "Until we use break"
                ),
                "Great! while checks the condition each time and continues as long as it's true.",
                "The while condition is checked before each round; it continues as long as it's true."
            ),
            SoalT(
                "What does break do?",
                listOf(
            "Skips the current round",
            "Completely ends the loop",
            "Closes the program",
            "Reverses the condition"
                ),
                "Exactly! break is like the exit door of the loop.",
                "break jumps completely out of the loop; continue only skips the current round."
            ),
            SoalT(
                "What's the difference between continue and break?",
                listOf(
            "They're the same",
            "continue skips the current round but the loop continues",
            "continue reverses the loop",
            "continue only works in for"
                ),
                "Great! continue means «this round isn't important, go to the next».",
                "continue skips the rest of the current body and starts the next round."
            ),
            SoalT(
                "What is the output of this loop?\nfor (i in 1..5) {\n    if (i == 3) continue\n    print(i)\n}",
                listOf(
            "12345",
            "1245",
            "12",
            "45"
                ),
                "Exactly! 3 was skipped with continue; the rest were printed.",
                "Only the number 3 is removed: 1 and 2 and 4 and 5 will be printed."
            ),
            SoalT(
                "Which is the healthy three-part pattern for a while loop?",
                listOf(
            "Start variable + condition + change inside body",
            "println + break + continue",
            "val + if + else",
            "Condition + condition + condition"
                ),
                "Great! Start, condition, change — without this trio, the loop becomes infinite.",
                "Without changing the variable inside the body, the condition never becomes false and the loop is infinite."
            ),
            SoalT(
                "var i = 0 and while (i < 3) { print(i) } — what's wrong with this code?",
                listOf(
            "Nothing is wrong",
            "i never increases; the loop is infinite",
            "i should be val",
            "The condition should be <="
                ),
                "Exactly! The body doesn't make the condition false; the program gets stuck.",
                "Because there's no i++ inside the body, the condition always stays true; infinite loop!"
            ),
            SoalT(
                "When does a while loop end?",
                listOf(
            "After 10 rounds",
            "When its condition becomes false",
            "Only when we see break",
            "Never"
                ),
                "Exactly! It runs as long as the condition is true; that's why you need to make the condition false somewhere.",
                "while continues as long as the condition is true; the condition must eventually become false."
            ),
            SoalT(
                "Which word is used to exit from the middle of a loop?",
                listOf(
            "return",
            "break",
            "stop",
            "end"
                ),
                "Great! break breaks the loop right there.",
                "break ends the loop immediately; continue only skips this round."
            )
            ),
            listOf(
            "                \"Counter with while: until user enters zero",
            "                \"Skipping one round with continue"
            )
        ),

        DarsT(
            "Functions: Packaging Code",
            listOf(
            "A function is like an automatic machine: it takes input, does something, and gives an answer back. You make a function with fun: fun jam(a: Int, b: Int): Int { return a + b }. Here a and b are parameters, the last : Int tells the answer type, and return means «bring back this value and work is done».",
            "Function benefit: you write it once and call it wherever you need. println is itself a ready-made function that we call! When you break code into small functions, each piece does one thing well, the code becomes readable, and if there's a bug, you only fix it in one place.",
            "Two nice shortcuts: first, default parameter value; fun salam(esm: String, peygham: String = \"سلام\") — if you don't give peygham when calling, it uses \"سلام\" itself. Second, single-line body: instead of return with braces, you can write fun zarb(a: Int, b: Int) = a * b and Kotlin understands the answer type itself.",
            "Execution note: the code inside the function body doesn't run at all until the function is called! Defining a function is like writing a recipe; until someone calls the cooking, no food gets cooked. That's why functions are usually defined at the top of the file and called from main."
            ),
            listOf(
            SoalT(
                "Which word is used to make a function in Kotlin?",
                listOf(
            "func",
            "fun",
            "def",
            "function"
                ),
                "Great! Kotlin shortened it: fun.",
                "Kotlin's keyword for functions is fun; func is from Swift and def is from Python."
            ),
            SoalT(
                "In fun jam(a: Int, b: Int): Int — what does the last Int express?",
                listOf(
            "Parameter type",
            "Function answer type",
            "Function name",
            "Default value"
                ),
                "Exactly! After the parentheses, comes colon and output type.",
                "Int after the last colon is the type of the value that gets returned."
            ),
            SoalT(
                "fun zarb(a: Int, b: Int) = a * b — what does this writing style say?",
                listOf(
            "The function is incomplete",
            "Single-line body: the answer is that expression",
            "The function gives an error",
            "return is required"
                ),
                "Great! For single-line bodies, the equal sign is enough.",
                "Expression body: the function answer is the value after =; Kotlin understands the type itself."
            ),
            SoalT(
                "fun salam(esm: String, peygham: String = \"سلام\") — what does salam(\"علی\") print?",
                listOf(
            "سلام علی",
            "علی سلام",
            "Error; peygham is missing",
            "سلام"
                ),
                "Exactly! You didn't give peygham, so the default value \"سلام\" was used.",
                "A parameter with default value can be omitted; salam(\"علی\") becomes: سلام علی"
            ),
            SoalT(
                "When does the code inside a function body run?",
                listOf(
            "At the very beginning of the program",
            "When the function is called",
            "Every night",
            "When we write return"
                ),
                "That's right! Function definition is just preparation; calling it is what makes it run.",
                "The function body only runs when it's called."
            ),
            SoalT(
                "What does println(jam(2, 3)) print?",
                listOf(
            "jam(2, 3)",
            "5",
            "23",
            "Error"
                ),
                "Correct! First jam calculates (5), then the result is printed.",
                "Functions inside parentheses run first; jam(2, 3) becomes 5 and println prints it."
            ),
            SoalT(
                "What help does a function with return provide?",
                listOf(
            "Ends the program",
            "Sends the calculated answer back to the caller to use",
            "Clears memory",
            "Hides errors"
                ),
                "Exactly! The function output is like an exam grade: it comes back to be used.",
                "return brings the value back instead of calling: val m = moadel(18, 20)."
            ),
            SoalT(
                "Why do we write functions instead of repeating code?",
                listOf(
            "To increase file size",
            "We write once, call a thousand times; fix is only in one place",
            "Because Kotlin requires functions",
            "To slow down the program"
                ),
                "Great! Function means template: build once, use infinitely.",
                "Function prevents repetition; fix is also done in only one place."
            )
            ),
            listOf(
            "                \"Function with two parameters: rectangle area",
            "                \"Function without output: just doing it"
            )
        ),

        DarsT(
            "Lists: Multiple Values in One Box",
            listOf(
            "So far, each variable has held one value. But many times we have multiple things of the same type: grades in a class, player names on a team, shopping list. A list is exactly for that: a box that holds multiple values together. You create a fixed list with listOf and a changeable list with mutableListOf.",
            "We access items by index, and indexes start from zero! nomreHa[0] is the first item, nomreHa[1] is the second, and so on. You get the list size with size: nomreHa.size. If the index goes out of bounds (like asking for the fifth item in a 4-item list), your program will give an error, so always pay attention to size.",
            "Changeable lists have tools: add adds an item, removeAt removes by index, indexOf tells you where a value is in the list (gives -1 if not found), and tells you if an item exists. Walking through a list is also done with a for loop: for (n in nomreHa) — no hassle with indexes, it goes through each item by itself.",
            "Two ready-to-use functions: sum gives the sum of all items, and reversed reverses the list. The classic pattern: summing with a loop — var majmoo = 0 and then for each item majmoo += n — the same thing you learned in the loops lesson, now on lists. In real projects, grades and data always live in lists."
            ),
            listOf(
            SoalT(
                "What is the index of the first item in a list?",
                listOf(
            "1",
            "0",
            "-1",
            "Depends on the list"
                ),
                "Great job! In Kotlin, counting indexes starts from zero.",
                "The first item is nomreHa[0]; indexes start from zero."
            ),
            SoalT(
                "What's the difference between listOf and mutableListOf?",
                listOf(
            "No difference",
            "listOf is fixed and mutableListOf allows adding and changing",
            "listOf is faster",
            "mutableListOf is only for numbers"
                ),
                "Exactly! mutable means changeable; like val and var but for lists.",
                "With listOf you can't add items; with mutableListOf you can."
            ),
            SoalT(
                "val n = listOf(10, 20, 30); what is n.size?",
                listOf(
            "3",
            "2",
            "30",
            "10"
                ),
                "Yes! size tells how many items there are; here we have three items.",
                "size tells how many items the list has: 3."
            ),
            SoalT(
                "What does nomreHa.add(12) do?",
                listOf(
            "Replaces the first item with 12",
            "Adds 12 to the end of the list",
            "Empties the list",
            "Gives an error"
                ),
                "Correct! add always adds to the end of the list.",
                "add places the new item at the end of the list."
            ),
            SoalT(
                "If nomreHa has four items and we write nomreHa[4], what happens?",
                listOf(
            "Gives the fourth item",
            "Gives zero",
            "We get an index error",
            "The list grows"
                ),
                "Exactly! The allowed indexes here are 0 to 3; 4 is out of range.",
                "Remember: the last allowed index is size - 1; 4 gives an error for a 4-item list."
            ),
            SoalT(
                "What does for (n in nomreHa) do?",
                listOf(
            "Counts from 0 to size",
            "Goes through the list item by item",
            "Prints the list",
            "Gives an error"
                ),
                "Great! A for loop on a list gives you the items one by one.",
                "For lists, for directly goes through items; no need for indexes."
            ),
            SoalT(
                "What is the output of this code?\nval list = listOf(5, 8, 2)\nprintln(list[1].toString())",
                listOf(
            "5",
            "8",
            "2",
            "Error"
                ),
                "Great! Index 1 means the second item.",
                "Indexes start from zero; list[1] is the second item which is 8."
            ),
            SoalT(
                "What does list.size give?",
                listOf(
            "Sum of items",
            "Number of items",
            "Last item",
            "Biggest item"
                ),
                "Correct! size means how many items are inside the list.",
                "size gives the number of items; sum gives their total."
            )
            ),
            listOf(
            "                \"add and removeAt on changeable lists",
            "                \"ready sum and reversed"
            )
        ),

        DarsT(
            "Classes and Objects",
            listOf(
            "A class is the blueprint for something, and an object is what is made from that blueprint. For example, the Daneshamooz class is the blueprint: each student has a name and a grade; and from it you can build 100 objects: Ali with grade 18, Sara with grade 20 and so on. The blueprint is written once and thousands of objects are made from it.",
            "Defining a class: class Daneshamooz(val esm: String, var nomre: Int) — the parentheses after the class name are the \"constructor\": when an object is created, it must get these properties. val means this property can't be changed later (a student's name!) and var means it can be changed (a grade can be changed!).",
            "Creating an object is done by calling the class name like a function: val d = Daneshamooz(\"Ali\", 18). Now with a dot you reach the properties and methods: d.esm gives the name, d.nomre gives the grade, and if you've defined a method, you call d.taarif(). Methods are functions that live inside a class and work on that object.",
            "Inside methods you can read and change the properties of that same object; with nothing extra: fun ezafeKon(chand: Int) { nomre += chand } — here nomre means the grade of this very object that the method is called on. This ability is the heart of object-oriented programming: data and the things related to it are collected together in one class."
            ),
            listOf(
            SoalT(
                "What's the difference between a class and an object?",
                listOf(
            "No difference",
            "A class is a blueprint and an object is something made from that blueprint",
            "Classes are used in Android but objects aren't",
            "An object is always bigger than a class"
                ),
                "Exactly! A class is a template; from one template you can make thousands of objects.",
                "A class is a definition (blueprint) and an object is an instance made from that blueprint."
            ),
            SoalT(
                "class Daneshamooz(val esm: String, var nomre: Int) — why is nomre with var?",
                listOf(
            "Because it's a number",
            "Because the grade is supposed to change later",
            "It was random; doesn't matter",
            "Because var is faster"
                ),
                "Great! Changeable properties must be var; like a grade that changes after reconsideration.",
                "val means locked, var means changeable; grade must be var so we can change it."
            ),
            SoalT(
                "How do we create an object from the class above?",
                listOf(
            "val d = Daneshamooz(\"Ali\", 18)",
            "val d = new Daneshamooz()",
            "val d = class Daneshamooz",
            "Daneshamooz.d = (\"Ali\", 18)"
                ),
                "Correct! You call the class name like a function and give the constructor values.",
                "Creating objects in Kotlin is without new: class name + parentheses and values."
            ),
            SoalT(
                "What does d.esm give?",
                listOf(
            "The type of object",
            "The esm property of object d",
            "Calls the esm function",
            "The class name"
                ),
                "Great! The dot means go inside the object and bring this property.",
                "We access object properties with a dot: d.esm is \"Ali\"."
            ),
            SoalT(
                "Inside the ezafeKon method, what does nomre refer to the grade of which object?",
                listOf(
            "All objects",
            "The object that the method is called on",
            "The first object created",
            "None; gives an error"
                ),
                "Exactly! Each object has its own version of properties; d.ezafeKon(2) increases Ali's grade not Sara's.",
                "The method works on the object it's called through."
            ),
            SoalT(
                "What is the output of println(d.esm) after d.ezafeKon(2)? (object d = Daneshamooz(\"Ali\", 18))",
                listOf(
            "Ali",
            "18",
            "20",
            "Error"
                ),
                "Yes! The grade changed but the name stayed the same; esm was with val.",
                "ezafeKon only changes nomre; esm stays Ali."
            ),
            SoalT(
                "Where is an Object created from?",
                listOf(
            "From file",
            "By calling the class name with parentheses: Ketab(\"name\", 100)",
            "From println",
            "Copied from val"
                ),
                "Great! A class is a blueprint and calling it creates an object.",
                "Class name with parentheses means creating a new object from that template."
            ),
            SoalT(
                "What's the difference between property and method?",
                listOf(
            "No difference",
            "Properties hold data; methods do actions",
            "Properties are faster",
            "Methods are only for text"
                ),
                "Exactly! Name and grade are properties; calculating average is a method.",
                "Properties are like files, methods are like things an employee knows how to do."
            )
            ),
            listOf(
            "                \"Class with two methods: small car",
            "                \"Multiple objects from one class; each has its own life"
            )
        ),

        DarsT(
            "Nullability: Fighting the Famous Bug",
            listOf(
            "null means «nothing»; it means this variable doesn't point to any value yet. The most famous error in programming, NullPointerException, comes from this: the program reaches null, thinks it has a value, works, and suddenly crashes! The creator of Kotlin called this the \"billion-dollar bug\" because this bug cost companies that much.",
            "Kotlin's solution: right from the start, decide if a variable can be null or not! The regular type (String) never accepts null; but if you add a question mark (String?), it means \"this can be text or null\". val laqab: String? = null is completely allowed, but val laqab: String = null will give a compile error; that's when and where the problem is caught.",
            "We work with nullable variables carefully and have three tools: ?. means safe call — if the value is null, it returns null instead of crashing: laqab?.length. ?: (they call it Elvis because it looks like Elvis Presley's hair!) means \"if null, use this instead\": laqab ?: \"no-title\". And !! means \"I swear it's not null!\" — if you're wrong, the program gives you that exact famous error; so only use !! when you're sure.",
            "This design means many bugs are caught during compilation before runtime. If a function only takes a String, you can't even give it null; the compiler stops you. This is called Null Safety, and it's one of the biggest reasons Kotlin is popular."
            ),
            listOf(
            SoalT(
                "What does null mean in programming?",
                listOf(
            "Zero",
            "Empty text",
            "Nothing; no value exists",
            "Compile error"
                ),
                "Great! null means this variable doesn't point to anything yet.",
                "null is different from zero and empty text; it means there's no value at all."
            ),
            SoalT(
                "val laqab: String? = null — what does the question mark do?",
                listOf(
            "Ask the user",
            "It says this variable can be null",
            "Hides the null error",
            "The question mark is extra"
                ),
                "Exactly! The ? makes the type nullable.",
                "With ? we tell Kotlin this variable can be null too."
            ),
            SoalT(
                "What does laqab?.length give when laqab is null?",
                listOf(
            "Zero",
            "It gives an error",
            "null",
            "Empty text"
                ),
                "Yeah! ?. returns null calmly instead of crashing.",
                "Safe call with ?. When the value is null, it returns null; no error."
            ),
            SoalT(
                "What does laqab ?: \"no-title\" do?",
                listOf(
            "If laqab is null, it gives \"no-title\"",
            "Converts title to no-title",
            "Always gives \"no-title\"",
            "Joins two values together"
                ),
                "Exactly! Elvis means \"if empty, use this instead\".",
                "The ?: operator gives the alternative value when the left side is null."
            ),
            SoalT(
                "What risk does the !! operator have?",
                listOf(
            "No risk at all",
            "If the value is null, the program gives the famous null error",
            "Makes the program slow",
            "Clears the value"
                ),
                "Great! !! means \"responsibility is mine\"; if you're wrong, it crashes.",
                "!! bypasses the compiler; if the value is actually null, you get NullPointerException."
            ),
            SoalT(
                "Why is Kotlin strong at \"Null Safety\"?",
                listOf(
            "Because it hides null",
            "Because it catches many null bugs during compilation before runtime",
            "Because it converts null to zero",
            "Because it uses !! a lot"
                ),
                "Exactly! Kotlin's type system doesn't let null sneak in somewhere.",
                "Kotlin separates nullable/non-nullable, moving errors from runtime to compile time."
            ),
            SoalT(
                "What does null mean?",
                listOf(
            "Zero",
            "Nothing; no value exists",
            "Empty text",
            "Negative number"
                ),
                "Great! null means \"nothing is in the box\".",
                "null means no value exists; not zero, not empty text."
            ),
            SoalT(
                "How does the ?. (question mark dot) help?",
                listOf(
            "Makes the program faster",
            "If value isn't null, it runs the method; otherwise does nothing",
            "Deletes text",
            "Makes the error bigger"
                ),
                "Exactly! ?. is like a shield against null.",
                "?. only calls the method when there's a real value; it gracefully passes over null."
            )
            ),
            listOf(
            "                \"?: Alternative value for null",
            "                \"Checking with if: the classic way"
            )
        ),

        DarsT(
            "Project: Small Calculator",
            listOf(
            "It's time to put everything we've learned together! In this project, we'll create a calculator that takes two numbers and an operator from the user: first number, second number, then the operator (+ or - or * or /). Then we use when to decide which operation to perform and print the result nicely.",
            "Look at the code structure: we have two helper functions (read number and safe division) and call them from main. Input conversion is done with toInt() and for division, we check that the denominator isn't zero first to avoid errors. This is engineering thinking: add a safety condition before doing something risky.",
            "A cool thing: we used a string as the subject in when; when works with any type, not just numbers! And the else branch catches users who type weird operators: 'Invalid operation'. Good programming means thinking about all cases; even cases where users make mistakes.",
            "In the playground for this app, fill the 'Input' box like this (three lines): first number, second number, operator. For example: 12 and 4 and /. The output should be 'Result: 3.0'. Now you try: how can you test multiplication? And if you want to add the modulo operator (%) too, which branch would you add to when?"
            ),
            listOf(
            SoalT(
                "How does our calculator identify the user's operator?",
                listOf(
            "nested if",
            "when (op)",
            "for",
            "while"
                ),
                "Exactly! when works on strings too: + and - and * and / each get their own branch.",
                "Since we have four specific cases, when (op) is the cleanest choice."
            ),
            SoalT(
                "Why did we put the condition b == 0 before division in the taqsim function?",
                listOf(
            "because Kotlin doesn't allow it",
            "because division by zero causes an error and we handle it with a proper message",
            "because zero isn't a positive number",
            "for speed"
                ),
                "Bravo! A professional programmer prevents accidents before they happen.",
                "Dividing an integer by zero stops the program with an error; so we check the condition first."
            ),
            SoalT(
                "What's the difference between a.toDouble() / b and a / b?",
                listOf(
            "no difference",
            "the first gives decimal division; the second gives integer division",
            "the second is more precise",
            "the first rounds"
                ),
                "That's right! By converting one side to Double, we get decimal division.",
                "In the operators lesson, we saw: 7 / 2 becomes 3 but 7.toDouble() / 2 becomes 3.5."
            ),
            SoalT(
                "When does the else branch in the calculator's when get executed?",
                listOf(
            "never",
            "when the user types an operator other than the four operations",
            "when we give a negative number",
            "second round"
                ),
                "Exactly! else catches all unexpected inputs.",
                "If op is not +, not -, not * and not /, the message 'Invalid operation' is printed."
            ),
            SoalT(
                "What's the correct input order to test the calculator in the playground?",
                listOf(
            "operator, first number, second number",
            "first number, second number, operator",
            "all in one line",
            "order doesn't matter"
                ),
                "Bravo! Just the order the readlns are called in the code.",
                "The code takes two numbers first, then the operator; so input box: 12 and 4 and /"
            )
            ),
            listOf(

            )
        ),

        DarsT(
            "Next Step: Where to From Here?",
            listOf(
            "Congratulations! Now you have the complete foundations of Kotlin: variables and types, conditions and when, loops, functions, lists, classes, and null safety. This is exactly what you need to start real programming; what you were missing was experience and projects; something you can now build!",
            "First recommendation: The official Kotlin reference at kotlinlang.org is the official, free, and up-to-date tutorials and documentation. For daily practice, you can also open this app, write code in the playground, and redo the exercises; a small daily practice is stronger than occasional reading.",
            "For book reading, \"Kotlin in Action\" by Dmitry Jemerov and Svetlana Isakova (published by Manning) is the world-renowned reference; its authors are from the Kotlin team itself. Of course, it's in English; if you're looking for Persian resources, the Coding Community channel has a step-by-step learning path you can follow.",
            "Where to next? Three big doors are open before you: First, Android — building real apps with user interface (using Kotlin). Second, Algorithms and problem-solving — programming competitions that sharpen the mind. Third, other languages — like Rust which is being taught in the Coding channel. All three start from the foundation you've built. Starting today, set a small goal and write your first program!"
            ),
            listOf(
            SoalT(
                "What is the official and free website for Kotlin tutorials?",
                listOf(
            "kotlinlang.org",
            "kotlinsite.com",
            "kotlin-ir.ir",
            "wikipedia.org"
                ),
                "Great! kotlinlang.org is the official Kotlin reference.",
                "Official Kotlin documentation and tutorials are published on kotlinlang.org."
            ),
            SoalT(
                "Which publisher released the famous \"Kotlin in Action\" book?",
                listOf(
            "Manning",
            "O'Reilly",
            "No Starch Press",
            "Wrox"
                ),
                "Exactly! Kotlin in Action was published by Manning.",
                "The Kotlin in Action book (by Jemerov and Isakova) is published by Manning."
            ),
            SoalT(
                "Which habit is more effective for progress in programming?",
                listOf(
            "Small daily practice",
            "Heavy studying once a month",
            "Just watching videos without coding",
            "Waiting for a big idea"
                ),
                "Great! Small but daily practice is the path of professionals.",
                "Daily consistency, even for ten minutes, has more impact than occasional heavy sessions."
            ),
            SoalT(
                "In lesson 15's code, what does the for loop loop through?",
                listOf(
            "Range 1 to 3",
            "Indices of masirHa list",
            "Characters of the text",
            "Functions"
                ),
                "Correct! 0 until masirHa.size means from index 0 to one before the last.",
                "With 0 until size, we loop through the list indices to have both row number and content."
            )
            ),
            listOf(

            )
        ),

        DarsT(
            "توابع پیشرفته: پارامتر پیش‌فرض و تابع بازگشتی",
            listOf(
            "تا حالا توابعی نوشتی که همهٔ ورودی‌هایشان را همیشه می‌دادیم. کاتلین راه هوشمندانه‌تری دارد: پارامتر پیش‌فرض. اگر موقع تعریف تابع بنویسی salam(esm: String = \"دوست\")، هر جا esm ندهی، خودش «دوست» را به کار می‌برد. مثل دکمهٔ تنظیمات گوشی که اگر دست نزنی روی حالت پیش‌فرض می‌ماند.",
            "پارامتر پیش‌فرض برای چه به کار می‌آید؟ وقتی یک ورودی در بیشتر فراخوانی‌ها همان است. مثلاً تابع خوش‌آمدگویی اپ مدرسه: روزهای عادی پیام «موفق باشی» کافی است، ولی ما می‌خواهیم گاهی پیام شخصی بفرستیم؛ پس پیش‌فرض را «موفق باشی» می‌گذاریم و فقط موقع نیاز عوضش می‌کنیم.",
            "حالا نکتهٔ حرفه‌ای‌تر: یک تابع می‌تواند خودش را صدا بزند! به این می‌گویند «بازگشتی» (Recursion). مثل وقتی توی آینه روبه‌روی آینه بایستی: داخل آینه، باز آینه است و داخل آن باز آینه... تا بی‌نهایت. در تابع بازگشتی باید یک «شرط توقف» باشد وگرنه مثل آینه‌های روبه‌رو تا بی‌نهایت ادامه پیدا می‌کند و برنامه از حافظه می‌افتد.",
            "مشهورترین مثال بازگشتی، فاکتوریل است: فاکتوریل ۵ یعنی ۵×۴×۳×۲×۱. چطور حسابش کنیم؟ ۵ ضربدر «فاکتوریل ۴»! و فاکتوریل ۴ برابر ۴ ضربدر «فاکتوریل ۳»... تا برسیم به فاکتوریل ۱ که جوابش ۱ است — همان شرط توقف. یعنی مسئلهٔ بزرگ، هر بار به نسخهٔ کوچک‌ترِ خودش می‌شکند.",
            "کجاها بازگشتی واقعاً می‌درخشد؟ مسائل درختی: جست‌وجو در پوشه‌های تو در تو، شمردن همهٔ زیرشاخه‌های یک درخت، حل ماز. فعلاً مهم این است که مفهومش را بفهمی؛ در درس الگوریتم‌ها دوباره به آن برمی‌گردیم."
            ),
            listOf(
            SoalT(
                "پارامتر پیش‌فرض چه کمکی می‌کند؟",
                listOf(
            "برنامه سریع‌تر اجرا می‌شود",
            "اگر ورودی داده نشود، خودش مقدار پیش‌فرض را به کار می‌برد",
            "تابع را بی‌نهایت اجرا می‌کند",
            "حافظه را پاک می‌کند"
                ),
                "آفرین! با پیش‌فرض، فراخوانی‌های ساده‌تر هم ممکن می‌شود.",
                "پارامتر پیش‌فرض یعنی اگر ورودی ندهی، همان مقدار از قبل تعیین‌شده به کار می‌رود."
            ),
            SoalT(
                "تابع بازگشتی (بازگشتی) یعنی چه؟",
                listOf(
            "تابعی که دو بار تعریف شده",
            "تابعی که خودش را دوباره صدا می‌زند",
            "تابعی که هیچ ورودی نمی‌گیرد",
            "تابعی که فقط متن برمی‌گرداند"
                ),
                "دقیقاً! تابع خودش را با مسئلهٔ کوچک‌تر صدا می‌زند.",
                "تابع بازگشتی تابعی است که خودش را با مسئلهٔ کوچک‌تر صدا می‌زند."
            ),
            SoalT(
                "بدون شرط توقف، تابع بازگشتی چه می‌شود؟",
                listOf(
            "یک بار اجرا و تمام می‌شود",
            "خودکار متوقف می‌شود",
            "تا بی‌نهایت ادامه می‌یابد و برنامه می‌شکند",
            "سریع‌تر می‌شود"
                ),
                "درسته! شرط توقف نفسِ تابع بازگشتی است.",
                "بدون شرط توقف، بازگشت تا بی‌نهایت می‌رود و برنامه از حافظه می‌افتد."
            ),
            SoalT(
                "فاکتوریل ۴ با روش بازگشتی چطور حساب می‌شود؟",
                listOf(
            "۴ + ۳ + ۲ + ۱",
            "۴ ضربدر فاکتوریل ۳",
            "۴ تقسیم بر ۳",
            "۴ به توان ۳"
                ),
                "آفرین! هر مرحله، فاکتوریل عدد قبلی را می‌خواهد.",
                "n! = n × (n-1)! — پس فاکتوریل ۴ = ۴ × فاکتوریل ۳."
            ),
            SoalT(
                "خروجی این کد چیست؟\nfun salam(n: Int = 3): Int {\n    return n * 2\n}\nprintln(salam().toString())",
                listOf(
            "۶",
            "۳",
            "خطا می‌دهد؛ ورودی ندهیم خطاست",
            "۰"
                ),
                "دقیقاً! n ندادیم، پس پیش‌فرض ۳ به کار رفت؛ ۳×۲=۶.",
                "چون n پیش‌فرض ۳ دارد، salam() یعنی ۳×۲ که می‌شود ۶."
            ),
            SoalT(
                "کدام مورد مناسب‌ترین جا برای استفاده از پارامتر پیش‌فرض است؟",
                listOf(
            "ورودی که در بیشتر صدا زدن‌ها یکسان است",
            "ورودی که هر بار کاملاً فرق دارد",
            "ورودی که هیچ‌وقت استفاده نمی‌شود",
            "اسم خود تابع"
                ),
                "دقیقاً! مقداری که معمولاً ثابت است، پیش‌فرضش می‌کنیم.",
                "پیش‌فرض برای ورودی‌ای است که در بیشتر فراخوانی‌ها همان یک مقدار است."
            )
            ),
            listOf(
            "                \"پیش‌فرض‌های چندگانه: فقط یکی را عوض کن",
            "                \"شمارش معکوس بازگشتی: از n تا ۱"
            )
        ),

        DarsT(
            "Advanced when: Smart Choices",
            listOf(
            "Remember the when lesson? when is like a school detective: it takes a value and compares it against conditions, running the first branch that matches. Now let's see its real power: when conditions aren't simple, when becomes much cleaner than if.",
            "A branch can handle multiple cases at once: when (day) { 1, 2 -> ... } means \"if it's 1 or 2\". Perfect for categorizing: Saturday to Wednesday are \"school days\", Thursday and Friday are \"weekend\". Without when, you'd need two separate ifs; with when, one is enough.",
            "Most useful of all: comparing with ranges. When the condition is \"between 1 and 10\", writing in 1..10 is cleaner than two conditions. The grading system shines here: 17-20 is \"excellent\", 14-16 is \"good\", 10-13 is \"needs improvement\", the rest is \"poor\" — four lines, complete descriptive table!",
            "The else branch in when is like a safety net: anything that doesn't match any condition falls into else. If you can cover all cases, don't include else; but if unexpected values might come, always include else so your program doesn't break.",
            "Pro tip: when can return a value instead of a variable — meaning you can use it inside a variable definition: val situation = when { ... }. This makes code short and readable; we'll use it many times in this lesson's examples."
            ),
            listOf(
            SoalT(
                "In when, if two values are separated by a comma like 1, 2, what does that mean?",
                listOf(
            "It checks the sum of the two values",
            "If either of these two values is true, the branch runs",
            "Only the first value matters",
            "It's a grammar error"
                ),
                "Great! Comma means \"or\"; whichever is true is enough.",
                "Multiple values with comma means if either is true, that branch runs."
            ),
            SoalT(
                "What does in 17..20 mean in when?",
                listOf(
            "The value is exactly 17 or 20",
            "The value is between 17 and 20 (both ends included)",
            "The value is greater than 20",
            "The value is multiplied by 17"
                ),
                "Exactly! A closed range from 17 to 20; both 17 and 20 are included.",
                "in 17..20 means the range from 17 to 20 including both ends."
            ),
            SoalT(
                "When does the else branch in when run?",
                listOf(
            "Always first",
            "When no other condition is true",
            "When the program gives an error",
            "It never runs"
                ),
                "Correct! else is the backup for all unhandled cases.",
                "else runs when no previous branch is true."
            ),
            SoalT(
                "Why is when better than multiple ifs for a grade description table?",
                listOf(
            "Because it compiles faster",
            "Because it collects all cases in one neat, readable structure",
            "Because it clears memory",
            "Because errors are impossible in when"
                ),
                "Awesome! Readability is when's biggest advantage in tables.",
                "when keeps all cases together and organized; it's easier to look at the code."
            ),
            SoalT(
                "What is the output of this code?\nval n = 15\nval h = when (n) {\n    in 1..10 -> \"low\"\n    in 11..20 -> \"medium\"\n    else -> \"high\"\n}\nprintln(h)",
                listOf(
            "low",
            "medium",
            "high",
            "It gives an error"
                ),
                "Exactly! 15 is in the range 11 to 20.",
                "15 is between 11 and 20; so the second branch runs: medium."
            ),
            SoalT(
                "If the input value doesn't match any branch and there's no else, what happens?",
                listOf(
            "The program continues and does nothing",
            "Error or unpredictable behavior; better to add else",
            "The last branch automatically runs",
            "The program restarts"
                ),
                "Correct! else is when's safety net.",
                "Without else, an unmatched case means no branch runs; so don't forget else."
            )
            ),
            listOf(
            "                \"when without input: Free conditions",
            "                \"Restaurant menu with when\""
            )
        ),

        DarsT(
            "Nested Loops: Tables and Patterns",
            listOf(
            "You've seen one-dimensional loops: \"Print 10 times\". Now it's time for nested loops: a loop inside another loop. The outer loop takes one step, and the inner loop completes a full cycle; like a coach's workout: \"4 sets, 10 reps per set\" — the sets are the outer loop, and the reps per set are the inner loop.",
            "Counting steps is important: if the outer loop runs 4 times and the inner runs 5 times, the inner commands run 4×5=20 times. Nested loops are powerful but expensive: three loops of 100 each means a million operations! For a 10×10 multiplication table that's only 100, it's great; for millions, you need to think.",
            "The most educational use of nested loops is the multiplication table: for i from 1 to 10, for j from 1 to 10, print i×j. Each row of the table is one cycle of the outer loop. Learn this pattern well; it's used constantly in reports and table games.",
            "The star pattern is the second one: for each line, the inner loop prints that many stars and then println moves to the next line. Line 1 has 1 star, line 2 has 2 stars... the output becomes a beautiful triangle. For a square, the inner loop always runs the same number of times; for a triangle, it runs the same as the line number.",
            "Important note: use print (without ln) to arrange stars in one line and println to finish the line. Don't mix these up, or everything will end up on one line or the lines will be single-star!"
            ),
            listOf(
            SoalT(
                "The outer loop runs 3 times and the inner loop runs 4 times; how many times do the inner commands execute?",
                listOf(
            "7 times",
            "12 times",
            "3 times",
            "4 times"
                ),
                "Great! 3×4=12; multiply the number of loops.",
                "Number of inner executions = outer loops × inner loops = 3×4 = 12."
            ),
            SoalT(
                "In the star triangle pattern, how far does the inner loop (the stars) run?",
                listOf(
            "Always 10 times",
            "Up to the line number (khat)",
            "Up to 100",
            "Until the user types"
                ),
                "Exactly! Line 1 has 1 star, line 2 has 2 stars... so the inner loop limit is khat.",
                "For a triangle, the inner loop runs up to khat; for a square up to a fixed number."
            ),
            SoalT(
                "To arrange stars in one line, which function is needed?",
                listOf(
            "println",
            "print",
            "readln",
            "listOf"
                ),
                "Correct! print without ln stays on the same line.",
                "print stays on the same line; println finishes the line and goes to the next."
            ),
            SoalT(
                "What is the output of this code?\nfor (i in 1..2) {\n    for (j in 1..3) {\n        print(j.toString())\n    }\n    println()\n}",
                listOf(
            "123\n123",
            "123456",
            "132\n132",
            "12\n12\n12"
                ),
                "Great! Two lines, each with three numbers; the inner loop completes each cycle.",
                "Each outer loop cycle gives one complete line of 123; so two lines of 123."
            ),
            SoalT(
                "Which program correctly represents \"Review 5 lessons, 10 questions each\" with nested loops?",
                listOf(
            "One 50-time loop that rotates everything together",
            "Outer loop for lessons (5) and inner for questions (10)",
            "Two separate and sequential loops",
            "Outer loop 10 and inner 5"
                ),
                "Exactly! Outer = categories, inner = items in each category.",
                "Outer loop for categories (lessons) and inner for items in each category (questions)."
            ),
            SoalT(
                "Why are deeply nested loops (like 4 layers) dangerous?",
                listOf(
            "Because Kotlin only allows 3 layers",
            "Because the number of executions grows very quickly and becomes slow",
            "Because it gives an error",
            "Because you can't have variables"
                ),
                "Great! Four loops of 100 each means a hundred million operations; the program freezes.",
                "Each layer multiplies; deep nesting means millions of executions and severe slowdown."
            )
            ),
            listOf(
            "                \"Complete 3×3 multiplication table",
            "                \"Number tower: Number triangle"
            )
        ),

        DarsT(
            "Advanced Lists: Indexing, Searching, and Filtering",
            listOf(
            "You know the basics of lists: listOf creates them, add adds items, size counts them. Now it's time for heavy-duty tools. First, indexing: every item in a list has a mailbox number that starts from zero! adadHa[0] is the first item, adadHa[1] is the second, and adadHa[adadHa.size - 1] is the last one. The famous \"index out of bounds\" error happens when you ask for a mailbox number that doesn't exist.",
            "The for loop can loop through the indices themselves: for (i in adadHa.indices) — then adadHa[i] means the i-th item. When is this useful? When both the current item and its position matter, or when you want to change an item: adadHa[i] = 99 replaces the i-th item (only works on mutableListOf!).",
            "Searching has two ready-made tools: indexOf gives the position of the first match (returns -1 if not found) and contains just says yes or no. For example, in a school attendance list: if (hazrin.contains(\"سارا\")) means Sarah is present.",
            "Filtering is a bigger skill: from a list, take only those that meet a condition. Kotlin has a ready-made tool called filter, but we'll build our own to understand the algorithm: create a new empty list, loop through the original list, and add each qualifying item to the new list with add. These three lines form the basis of thousands of reporting programs.",
            "Two small but useful functions: sum that adds all numbers and reversed that flips the list. You can build average yourself: sum divided by size. Remember that integer division drops the decimal part; for accurate averages, work with Doubles."
            ),
            listOf(
            SoalT(
                "What is the index of the first item in a list?",
                listOf(
            "۱",
            "۰",
            "بستگی به طول لیست دارد",
            "-۱"
                ),
                "Exactly! Indexing starts from zero.",
                "Indexing in Kotlin starts from 0; the first item is list[0]."
            ),
            SoalT(
                "What does indexOf return if it doesn't find the item?",
                listOf(
            "۰",
            "اندیس آخر لیست",
            "-۱",
            "خطا می‌دهد"
                ),
                "Bravo! -1 means \"not found\"; always check for it.",
                "indexOf returns -1 when it doesn't find the item."
            ),
            SoalT(
                "Which line changes the third item in list a to 99?",
                listOf(
            "a[2] = 99",
            "a[3] = 99",
            "a.add(99)",
            "a.size = 99"
                ),
                "Correct! The third item has index 2 (we count from zero).",
                "The third item means index 2: a[2] = 99."
            ),
            SoalT(
                "What are the three steps of manually filtering a list?",
                listOf(
            "مرتب کن، حذف کن، چاپ کن",
            "لیست خالی بساز، حلقه بزن، شرط‌دارها را add کن",
            "reverse کن، sum کن، چاپ کن",
            "اندیس بگیر، پاک کن، دوباره بساز"
                ),
                "Exactly! These three steps are the basis of filtering.",
                "Filter = empty destination list + loop through source + add qualifying items."
            ),
            SoalT(
                "What is the average of 10 and 20 using integer division (Int)?",
                listOf(
            "۱۵",
            "۱۵٫۰",
            "۱۴",
            "خطا می‌دهد"
                ),
                "Here it's 30/2=15; but if there's a decimal part, integer division drops it.",
                "Integer division (Int) drops the decimal part; use Double for accuracy."
            ),
            SoalT(
                "What's the difference between listOf and mutableListOf?",
                listOf(
            "هیچ فرقی ندارند",
            "اولی ثابت است و عضو نمی‌پذیرد؛ دومی قابل تغییر است",
            "اولی برای متن است دومی برای عدد",
            "دومی سریع‌تر است"
                ),
                "Bravo! val ≠ immutable list; the list created by listOf is locked.",
                "listOf creates a locked list; mutableListOf allows add and modification."
            )
            ),
            listOf(
            "                \"Index-based traversal: position and value together",
            "                \"Finding the biggest number with an indexed loop"
            )
        ),

        DarsT(
            "Algorithms: Bubble Sort",
            listOf(
            "An algorithm is a precise recipe for solving a famous problem. \"Sorting a list\" is one of the oldest problems in computer science; your phone photos, report card grades, competition results — sorting is everywhere. The first and simplest sorting algorithm is \"Bubble Sort\".",
            "Bubble Sort's idea is fun and simple: look at two neighbors; if they're in the wrong order, swap them. Go from the beginning to the end of the list; this is called \"one pass\". In each pass, the largest unsorted item bubbles up to the end of the list — that's why it's called Bubble Sort!",
            "Implementation: two nested loops. The outer loop goes through the items, the inner loop compares and swaps neighbors. To swap, you need a \"temporary glass\": temp = a; a = b; b = temp. Without the temporary glass, one of the values gets lost!",
            "How slow is it? In the worst case, about n² comparisons: 100 items means about 10,000 comparisons, 10,000 items means 100 million! That's why faster algorithms like Quick Sort were created. But Bubble Sort is unbeatable for understanding the concept of sorting — and it's perfectly fine for small lists.",
            "The big lesson of this lesson: you can practice any algorithm on paper before writing code. Write some numbers on paper, step through them like a computer, and watch how they get sorted. When you get the answer on paper, the code is just three lines more."
            ),
            listOf(
            SoalT(
                "What is the main idea of Bubble Sort?",
                listOf(
            "تقسیم لیست به دو نیمه",
            "مقایسه و جابه‌جایی دو همسایه در هر گام",
            "حذف اعداد اضافه",
            "جمع همهٔ اعداد"
                ),
                "Exactly! It compares neighbors and swaps them if they're in the wrong order.",
                "Bubble Sort = comparing neighbors one by one and swapping them until sorted."
            ),
            SoalT(
                "Why do we need a temporary variable for swapping two variables?",
                listOf(
            "برای سرعت بیشتر",
            "چون بدون آن یکی از دو مقدار از بین می‌رود",
            "چون کاتلین اجازه نمی‌دهد مستقیم عوض کرد",
            "برای خوانا بودن کد"
                ),
                "Correct! Like swapping the contents of two glasses: first pour one into a third glass.",
                "temp = a; a = b; b = temp — without temp, the value of a is lost."
            ),
            SoalT(
                "Why is this algorithm called \"Bubble\"?",
                listOf(
            "چون در آب کار می‌کند",
            "چون در هر دور، مقدار بزرگ‌ترین مثل حباب به ته لیست بالا می‌آید",
            "چون حافظه‌اش حبابی است",
            "چون برنامه‌نویس‌ها آن را دوست داشتند"
                ),
                "Bravo! In each pass, the largest remaining item bubbles to the end of the list.",
                "In each complete pass, the largest unsorted item settles at the end of the list; like a bubble rising."
            ),
            SoalT(
                "Bubble Sort's complexity is about n²; for 1000 items, how many comparisons?",
                listOf(
            "۲۰۰۰",
            "۱۰ هزار",
            "۱ میلیون",
            "۱۰۰۰"
                ),
                "Exactly! 1000×1000 = one million; that's why faster algorithms were needed.",
                "1000 to the power of 2 = 1,000,000 comparisons in the worst case."
            ),
            SoalT(
                "What is the output of this code?\nval a = mutableListOf(3, 1)\nval t = a[0]\na[0] = a[1]\na[1] = t\nprintln(a.toString())",
                listOf(
            "[3, 1]",
            "[1, 3]",
            "[1, 1]",
            "[3, 3]"
                ),
                "Bravo! Successful swap with temporary variable t.",
                "With a temporary variable, two items are swapped without loss: [1, 3]."
            ),
            SoalT(
                "Before writing code for an algorithm, what's the best practice?",
                listOf(
            "کد را حفظ کنی",
            "روی کاغذ با چند نمونه قدم‌به‌قدم اجرایش کنی",
            "منتظر شانس بمانی",
            "همان اول سریع‌ترین الگوریتم دنیا را بنویسی"
                ),
                "Correct! Paper practice means half the work is done.",
                "First practice the algorithm on paper with a few examples, then write the code."
            )
            ),
            listOf(
            "                \"Tracking the second pass of Bubble Sort on 4, 1, 3",
            "                \"Descending sort (big to small): just one sign change!"
            )
        ),

        DarsT(
            "Professional Classes: Properties and Behaviors",
            listOf(
            "You got to know classes in chapter 3: the template we use to create objects. Now let's get serious about classes. A professional class has two things: \"properties\" (data it holds: balance, name, grades) and \"behaviors\" (things it does: deposit, withdraw, display). A class is like a professional employee: it has both records and a to-do list.",
            "Encapsulation rule: sensitive data should stay locked inside the class and only be manipulated through its methods. The bank balance shouldn't be set to zero from outside; it must go through the withdraw method that first checks if there's enough balance. This way, the class protects itself from breaking.",
            "Methods can call \"this\": meaning \"the very object we're working on right now\". Inside the deposit method, this.balance means the balance of this very account. This word becomes vital when a method parameter has the same name as a property or when you want to return the object itself.",
            "Objects can work together! A Madrese class that holds a list of DaneshAmooz objects and has a method to calculate the school's average. This composition (a class holding other objects) is the backbone of all real apps: chat pages, shopping carts, gradebooks — they all follow this pattern.",
            "A design principle: one responsibility per class. A bank account class shouldn't know how to send emails and play games; it only does banking. When each class knows how to do one thing, finding bugs and adding features becomes easier — this is what makes the difference between chaos and order in big projects."
            ),
            listOf(
            SoalT(
                "What does encapsulation (locking data) mean?",
                listOf(
            "Hiding the class from the compiler",
            "Sensitive data only changes through controlled class methods",
            "Removing all properties",
            "Writing a class in one line"
                ),
                "Great! The class defends its data.",
                "Encapsulation means data only changes through the class's own controlled methods."
            ),
            SoalT(
                "What does \"this\" refer to inside a method?",
                listOf(
            "To the whole program",
            "To the very object that called the method",
            "To the parent class",
            "To main"
                ),
                "Exactly! \"this\" means \"this very object\".",
                "\"this\" means the object itself whose method is currently running."
            ),
            SoalT(
                "Why does the withdraw method check the balance first?",
                listOf(
            "For faster speed",
            "So the account doesn't go negative; the class defends against bad states",
            "Because Kotlin forced it",
            "For greeting"
                ),
                "Correct! Checking conditions protects the class's data.",
                "The method checks the validity of data before changing it, so the class doesn't enter a bad state."
            ),
            SoalT(
                "What's the benefit of \"one responsibility per class\"?",
                listOf(
            "The program becomes shorter",
            "Bugs are easier to find and code stays organized",
            "The program compiles faster",
            "No testing needed"
                ),
                "Awesome! Single responsibility = clean and maintainable code.",
                "When each class only knows how to do one thing, debugging and development become easier."
            ),
            SoalT(
                "What's the output of this code?\nclass Jabe(var x: Int) {\n    fun doBarabar() {\n        x = x * 2\n    }\n}\nval j = Jabe(5)\nj.doBarabar()\nj.doBarabar()\nprintln(j.x.toString())",
                listOf(
            "5",
            "10",
            "20",
            "25"
                ),
                "Exactly! Two doublings: 5→10→20.",
                "Each call to doBarabar doubles the value of x; 5 then 10 then 20."
            ),
            SoalT(
                "For \"a school that has a list of students and calculates the overall average\", which design is correct?",
                listOf(
            "Put all grades in one long variable",
            "A Madrese class that holds a list of DaneshAmooz objects and has a moadelKol method",
            "Write a separate app for each student",
            "Don't use classes; just use global variables"
                ),
                "Exactly! Objects inside objects — the main pattern of real apps.",
                "A container class holding a list of objects + calculation method = correct object-oriented design."
            )
            ),
            listOf(
            "                \"Object inside Object: School and Students",
            "                \"Little Address Book: Searching through Object Lists"
            )
        ),

        DarsT(
            "Big Project: Number Guessing Game",
            listOf(
            "Time to summarize! Let's put everything you've learned so far into one real project: a number guessing game. The player has a secret number (1-50); the program guesses and says higher or lower; until the guess is right. At the end, it announces the number of tries and congratulates if it was few.",
            "Why is this project golden? Because each piece of it is from previous lessons: variables for the secret number and try counter (lesson 4), while for repeating until the right guess (lesson 11), user input with readln and toInt conversion (lesson 7), if/else for higher/lower (lesson 8), and text template with \$ for friendly messages (lesson 4). Programming is just combining these pieces.",
            "In the playground, readln reads from the \"input\" box: each line is one input. So to test the game, write several guesses in advance in the input box (like 25, then 37, then 31) and see what the game says to each. This is exactly how professional programmers test: specific inputs, specific expectations.",
            "Design tip: Game messages shouldn't be dry. \"Lower!\" is just an answer; \"Go a bit lower 🔻\" feels like a game. Good software isn't just correct; it's friendly. These details separate popular apps from dry ones.",
            "After successful execution, two challenges for you: first — put a 7-try limit (with a counter and condition). second — if the user types text instead of number, don't let the program crash (safe toInt). These two pieces turn the simple game into a complete game."
            ),
            listOf(
            SoalT(
                "Which lessons have been combined in the number guessing game?",
                listOf(
            "Only loops",
            "Variables, while loop, input, conditions, and text templates",
            "Only classes",
            "Only lists"
                ),
                "Great! A project is combining learned pieces.",
                "Game = variables + while + readln + if/else + \$ text template."
            ),
            SoalT(
                "In the playground, where does readln read from?",
                listOf(
            "From phone messages",
            "From the input box; each line is one input",
            "From a file",
            "From the random button"
                ),
                "Exactly! Each line in the input box fills the next readln.",
                "The playground's input box is given line by line to readln."
            ),
            SoalT(
                "What's the condition for continuing the main game loop?",
                listOf(
            "while (tedad < 100)",
            "while (hads != mahfi) — until guess equals secret",
            "while (true) without stopping",
            "for (i in 1..5)"
                ),
                "Correct! As long as guess ≠ secret, game continues.",
                "The loop continues as long as the guess hasn't equalled the secret number."
            ),
            SoalT(
                "Why do we count the number of tries?",
                listOf(
            "To slow down the program",
            "For scoring and motivation; and for try limit in complete version",
            "Because Kotlin forced it",
            "For no reason"
                ),
                "Awesome! Counter both creates rewards and sets limits.",
                "The try counter is the basis for scoring and the 7-try limit."
            ),
            SoalT(
                "If the user types \"hello\" instead of a number, what happens to our simple code?",
                listOf(
            "It guesses itself",
            "toInt will error; we need to check safely",
            "It automatically becomes zero",
            "The program restarts"
                ),
                "Correct! User input must always be checked — project challenge 2.",
                "Converting non-numeric text with toInt causes error; professional code checks input."
            ),
            SoalT(
                "Which statement about game design is correct?",
                listOf(
            "Dry and formal messages are better",
            "Friendly and energetic messages make the game experience better",
            "Messages don't matter at all",
            "Messages should always be in English"
                ),
                "Exactly! Good software is friendly; \"🔺\" and encouragement create game feel.",
                "Friendly and encouraging messages complete the user experience."
            ),
            SoalT(
                "In the calculator project, why was when better than nested ifs?",
                listOf(
            "Because it uses memory",
            "Because it cleanly covers the four operations (+ - * /)",
            "Because it's faster",
            "Because it has no errors"
                ),
                "Great! Multiple choices = when's main job.",
                "For choosing between multiple options (four operations), when is the clearest way."
            ),
            SoalT(
                "In the calculator, what's the danger of division by zero?",
                listOf(
            "No danger at all",
            "Runtime error; you must check before",
            "It gives answer 100",
            "The program runs faster"
                ),
                "Correct! Good code checks the denominator before dividing.",
                "Division by zero causes error; check with if (b == 0) before."
            )
            ),
            listOf(
            "                \"Version with try limit (7 tries)",
            "                \"Random secret number (so it changes each time)",
            "                \"Calculator with user input"
            )
        ),

        DarsT(
            "Advanced Review: Class Gradebook",
            listOf(
            "The final lesson of the advanced section is a capstone project: the «Class Gradebook» — a program that keeps track of students' grades and reports: each kid's average, class average, top student, and count of passing students. All 22 skills in one organized program.",
            "Design Before Code: We need three things. First, «Data Model»: DaneshAmooz class with name and list of grades (classes for classes and lists). Second, «Calculations»: average method for each kid and loop for class average (lessons on functions and loops). Third, «Reporting»: finding the best by comparing in a loop (advanced lists lesson).",
            "Finding «Best» is a pattern you should know in your sleep: start with toofangar variable with the first element; in the loop, compare each element with it; if better, replace it. At the end of the loop, the variable is the best. Same pattern for worst, richest, fastest... — only the comparison condition changes.",
            "Conditional Counting: Passing means average above 10? Get a counter number zero, in the loop if condition is met, add one. This «conditional counting» pattern appears constantly in school reports, shopping, and sports.",
            "Look at the final code: Each part is small and its job is clear. Professional programming means exactly this: big program made of small, understandable pieces. If you don't know something, find the related lesson number and go back; the path taken is worth returning to."
            ),
            listOf(
            SoalT(
                "What's the first step before writing a multi-part program?",
                listOf(
            "Start typing immediately",
            "Design: specify data model, calculations and reports",
            "Just add a title",
            "Put everything inside main"
                ),
                "Great! Proper design is half the coding.",
                "Before code: specify data model + calculations + outputs on paper."
            ),
            SoalT(
                "How does the «finding best» pattern work?",
                listOf(
            "We sort the list and take the first one",
            "Variable starts with first element and in loop, better ones replace it",
            "We choose a random number",
            "We get best with indexOf"
                ),
                "Exactly! The current champion is compared with each new challenger.",
                "Best = variable starting with first element + comparison loop + replacement if better."
            ),
            SoalT(
                "What pattern does counting passers have?",
                listOf(
            "Size of whole list",
            "Zero counter that in loop increases by one each time condition is met",
            "Sum of grades",
            "No pattern"
                ),
                "Correct! Conditional counter is tool of all reports.",
                "Conditional counting = zero variable + loop + if condition + increment by one."
            ),
            SoalT(
                "In this lesson's project, where is moadel() written in the class and what does it return?",
                listOf(
            "Global variable; text",
            "Method inside class; numeric average (sum divided by size)",
            "Separate class; list",
            "main function; nothing"
                ),
                "Great! Average behavior belongs to the student itself; not outside class.",
                "moadel is a method inside DaneshAmooz class that returns numeric average."
            ),
            SoalT(
                "If kelas list is empty, what happens to line (jamMoadel / kelas.size)?",
                listOf(
            "It becomes zero",
            "Division by zero; error! You must check for emptiness",
            "It becomes 100",
            "It becomes infinite"
                ),
                "Exactly! Professional program checks for empty case too.",
                "Division by zero is error; before reporting, check if size > 0."
            ),
            SoalT(
                "Why is each part of the project kept small and clear?",
                listOf(
            "To keep file short",
            "So each piece is understandable; big program is made of small pieces",
            "Because Kotlin doesn't allow long",
            "To save electricity"
                ),
                "Correct! Understandable small pieces = healthy and fixable code.",
                "Big programs are always built from small, clear pieces."
            ),
            SoalT(
                "After finishing this course, what's the best way to continue?",
                listOf(
            "Delete the app",
            "Build personal projects: game, school tool, more professional calculator",
            "Just watch videos",
            "Wait for a miracle"
                ),
                "Great! Building real project is the biggest teacher.",
                "Continue path = building personal project; each project is a new lesson."
            ),
            SoalT(
                "To write Kotlin on real computer, what tool is needed?",
                listOf(
            "Only phone",
            "Android Studio or IntelliJ IDEA",
            "Only browser",
            "Calculator"
                ),
                "Exactly! Android Studio is the official environment for building Android apps.",
                "Android Studio (for app) and IntelliJ IDEA (for regular program) are Kotlin's official environments."
            )
            ),
            listOf(
            "                \"Adding feature: Count excellent ones (average 18 and above)",
            "                \"Your next challenge: Text report with \$ formatting",
            "                \"Project ideas for you"
            )
        )
    )

    val tamrinHa: List<TamrinT> = listOf(
        TamrinT(
            "First Hello",
            "Write a program that prints the text \"Hello World!\""
        ),

        TamrinT(
            "Adding Two Numbers",
            "Create two variables a = 12 and b = 30 and print their sum."
        ),

        TamrinT(
            "Even or Odd",
            "There is a variable adad = 7. If it's even print \"Even\" and if it's odd print \"Odd\"."
        ),

        TamrinT(
            "Counting Numbers",
            "Print numbers 1 to 5 with a for loop; each number on a new line."
        ),

        TamrinT(
            "Three Times Table",
            "Print multiples of 3 from 3 to 15 in one line with spaces: 3 6 9 12 15"
        ),

        TamrinT(
            "Square Function",
            "Write a function called morabaa that takes a number and returns its square; then print morabaa(6)."
        ),

        TamrinT(
            "Sum of Grades",
            "You are given a list of grades. Calculate the sum using a loop and print it."
        ),

        TamrinT(
            "Largest Number",
            "A list adadHa is given; find and print the largest number in it."
        ),

        TamrinT(
            "Countdown",
            "Print numbers from 3 down to 1, each on a new line, then print \"Launch!\""
        ),

        TamrinT(
            "Hello Everyone",
            "A list esmHa is given; for each name print a line \"Hello X\" where X is the name."
        )
    )
}
