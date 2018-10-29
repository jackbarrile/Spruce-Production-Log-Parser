package logger


fun main(args: Array<String>) {
    print("Please enter the full path to the CSV log file: ")
    val csvFileName: String = readLine()!!
    val spruceProductionLogParser = SpruceProductionLogParser(csvFileName)
    spruceProductionLogParser.readCsv()

    println("CSV file $csvFileName parsed successfully")

    var option = 0

    while (option != 4) {
        println("-------------------------------------")
        println("Option 1: Get page ranking by unique hits")
        println("Option 2: Get page ranking by number of users")
        println("Option 3: Get user by unique page views")
        println("Option 4: Exit")
        print("Please choose an option: ")

        option = readLine()!!.trim().toInt()

        if (option < 0 || option > 4) {
            println("Invalid option selected, exiting progam")
            System.exit(1)
        }

        if (option == 1) {
            for (i in spruceProductionLogParser.pageUniqueHitRanking) {
                println(i)
            }
        }
        if (option == 2) {
            for (i in spruceProductionLogParser.pageNumUniqueUsersRanking) {
                println(i)
            }
        }
        if (option == 3) {
            for (i in spruceProductionLogParser.usersUniqueViewRanking) {
                println(i)
            }
        }
    }

}