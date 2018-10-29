package logger.tests

import logger.SpruceProductionLogParser
import org.junit.Assert
import org.junit.Test

class SpruceProductionLogParserTest {

    /* Negative tests */
    @Test
    fun testFileNotFoundReturnsProperMessage() {
        try {
            SpruceProductionLogParser("/dne.csv").readCsv()
            assert(false)
        } catch (e: NoSuchFileException) {
            Assert.assertEquals("File /dne.csv does not exist", e.reason as String)
        }
    }

    @Test
    fun testTooManyValuesReturnsProperMessage() {
        try {
            SpruceProductionLogParser("src/test/resources/invalidLine.csv").readCsv()
            assert(false)
        } catch (e: Exception) {
            Assert.assertEquals("Line 1 has an invalid number of values. Expected 3 but was 4", e.message as String)
        }
    }

    @Test
    fun testInvalidTimestampReturnsProperMessage() {
        try {
            SpruceProductionLogParser("src/test/resources/invalidTimestamp.csv").readCsv()
            assert(false)
        } catch (e: Exception) {
            Assert.assertEquals("Text 'badTimeStamp' could not be parsed at index 0", e.message as String)
        }
    }

    /* Positive tests */
    @Test
    fun testGetPageRankingByUniqueHits() {
        val spruceProductionLoggerParser = SpruceProductionLogParser("src/test/resources/test.csv")
        spruceProductionLoggerParser.readCsv()
        Assert.assertEquals(3, spruceProductionLoggerParser.pageUniqueHitRanking.size)
        Assert.assertEquals("/index.html", spruceProductionLoggerParser.pageUniqueHitRanking[0])
        Assert.assertEquals("/index.jsp", spruceProductionLoggerParser.pageUniqueHitRanking[1])
        Assert.assertEquals("/users.html", spruceProductionLoggerParser.pageUniqueHitRanking[2])
    }

    @Test
    fun testGetPageRankingByNumOfUniqueUsers() {
        val spruceProductionLoggerParser = SpruceProductionLogParser("src/test/resources/test.csv")
        spruceProductionLoggerParser.readCsv()
        Assert.assertEquals(3, spruceProductionLoggerParser.pageNumUniqueUsersRanking.size)
        Assert.assertEquals("/index.html", spruceProductionLoggerParser.pageNumUniqueUsersRanking[0])
        Assert.assertEquals("/index.jsp", spruceProductionLoggerParser.pageNumUniqueUsersRanking[1])
        Assert.assertEquals("/users.html", spruceProductionLoggerParser.pageNumUniqueUsersRanking[2])
    }

    @Test
    fun testGetUserRankingByUniquePageViews() {
        val spruceProductionLoggerParser = SpruceProductionLogParser("src/test/resources/test.csv")
        spruceProductionLoggerParser.readCsv()
        Assert.assertEquals(5, spruceProductionLoggerParser.usersUniqueViewRanking.size)
        Assert.assertEquals("user5", spruceProductionLoggerParser.usersUniqueViewRanking[0])
        Assert.assertEquals("user1", spruceProductionLoggerParser.usersUniqueViewRanking[1])
        Assert.assertEquals("user4", spruceProductionLoggerParser.usersUniqueViewRanking[2])
        Assert.assertEquals("user3", spruceProductionLoggerParser.usersUniqueViewRanking[3])
        Assert.assertEquals("user2", spruceProductionLoggerParser.usersUniqueViewRanking[4])
        assert(true)
    }

}