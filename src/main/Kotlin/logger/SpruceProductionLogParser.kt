package logger

import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class SpruceProductionLogParser constructor(var csv: String) {

    private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss+00:00")

    /**
     *  These maps contain a a user or timestamp as the key and a count as the value.
     *  Maps were chosen here due to their quick insert and reads.
     */
    private var pageUniqueHitCounterMap: HashMap<String, Int> = HashMap()
    private var pageNumUniqueUsersCounterMap: HashMap<String, Int> = HashMap()
    private var usersUniqueViewCounterMap: HashMap<String, Int> = HashMap()

    /** These maps map either a timestamp or a user to a unique list of associated pages */
    private var timestampPageMap: HashMap<String, ArrayList<String>> = HashMap()
    private var userPageMap: HashMap<String, ArrayList<String>> = HashMap()

    /** These lists contain the rankings in descending order, compiled upon reading the CSV. They are populated ahead
     * of time so each get will be constant and quick */
    var pageUniqueHitRanking: ArrayList<String> = ArrayList()
    var pageNumUniqueUsersRanking: ArrayList<String> = ArrayList()
    var usersUniqueViewRanking: ArrayList<String> = ArrayList()

    /**
     * Parses the CSV file contained at the path provided in the constructor. Will throw a NoSuchFileException at
     * runtime if a file is not found
     */
    fun readCsv() {

        val csvFileObj = File(csv) // val is immutable, while var is not

        if (!csvFileObj.exists()) {
            throw NoSuchFileException(csvFileObj, reason = "File $csv does not exist")
        }

        /** Read a file line by line to preserve memory */
        var lineNum = 0
        csvFileObj.forEachLine { it ->
            if (lineNum == 0) { // skip the header row
                lineNum++
            } else {
                val result: List<String> = it.split(",").map { it.trim() }
                if (result.size != 3)
                /** typically, custom Exceptions would be created but Exception will do for now */
                    throw Exception("Line $lineNum has an invalid number of values. Expected 3 but was " + result.size)
                try {
                    parseLine(result)
                } catch (e: Exception) {
                    throw Exception(e.message)
                }

                lineNum++
            }
        }

        sortAndStoreRanking(pageUniqueHitCounterMap, pageUniqueHitRanking)
        sortAndStoreRanking(pageNumUniqueUsersCounterMap, pageNumUniqueUsersRanking)
        sortAndStoreRanking(usersUniqueViewCounterMap, usersUniqueViewRanking)
    }

    /**
     * Parses and stores a single line from the CSV
     */
    private fun parseLine(result: List<String>) {
        val fileName: String = result[0]
        val user: String = result[1]
        LocalDate.parse(result[2], DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss+00:00")) // verify the format is correct
        val timestamp: String = result[2].format(dateFormat)

        storePageUniqueHitRanking(fileName, timestamp)
        storePageNumUniqueUsersRanking(fileName, user)
    }

    /**
     * Given a fileName and timeStamp, this stores the timestamp in the pageUniqueHitCounterMap
     *   - ensuring its singular existence in that map
     */
    private fun storePageUniqueHitRanking(fileName: String, timeStamp: String) {
        if (!timestampPageMap.containsKey(timeStamp)) {
            val list = arrayListOf<String>()
            list.add(fileName)
            timestampPageMap[timeStamp] = list
            addToCounterMap(pageUniqueHitCounterMap, fileName)
        }

        if (!timestampPageMap[timeStamp]!!.contains(fileName)) {
            addToCounterMap(pageUniqueHitCounterMap, fileName)
            val list = timestampPageMap[timeStamp]
            list!!.add(fileName)
            timestampPageMap[timeStamp] = list
        }
    }

    /**
     * Given a fileName and user, this stores the user in the pageNumUniqueUsersCounterMap
     *   - ensuring its singular existence in that map
     */
    private fun storePageNumUniqueUsersRanking(fileName: String, user: String) {
        if (!userPageMap.containsKey(user)) { // user not yet stored, store them
            val list = arrayListOf<String>()
            list.add(fileName)
            userPageMap[user] = list
            addToCounterMap(pageNumUniqueUsersCounterMap, fileName)
            addToCounterMap(usersUniqueViewCounterMap, user)
        }

        if (!userPageMap[user]!!.contains(fileName)) {
            addToCounterMap(pageNumUniqueUsersCounterMap, fileName)
            addToCounterMap(usersUniqueViewCounterMap, user)
            val list = userPageMap[user]
            list!!.add(fileName)
            userPageMap[user] = list
        }
    }

    /**
     * A helper method that increments a counter for a particular key (user or timestamp)
     */
    private fun addToCounterMap(counterMap: HashMap<String, Int>, key: String) {
        if (counterMap[key] == null) {
            counterMap[key] = 1
        } else {
            counterMap[key] = counterMap[key]!!.plus(1) // !! does a null check
        }
    }

    /**
     * Given a countMap - a map which maps a user/page to its associated count - populate the appropriate ranking list.
     * This runs in O(3n) == O(n)
     */
    private fun sortAndStoreRanking(countMap: HashMap<String, Int>, rankingList: ArrayList<String>) {
        val sortedCountMap = countMap.toList().sortedBy { (_, value) -> value }.toMap()
        for ((key) in sortedCountMap) {
            rankingList.add(key)
        }
        rankingList.reverse() // Reverse the order since the sorting was done in descending order
    }

}


