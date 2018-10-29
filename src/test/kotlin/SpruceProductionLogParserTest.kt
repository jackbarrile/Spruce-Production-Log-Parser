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
        val spruceProductionLogParser = SpruceProductionLogParser("src/test/resources/test.csv")
        spruceProductionLogParser.readCsv()
        Assert.assertEquals(3, spruceProductionLogParser.pageUniqueHitRanking.size)
        Assert.assertEquals("/index.html", spruceProductionLogParser.pageUniqueHitRanking[0])
        Assert.assertEquals("/index.jsp", spruceProductionLogParser.pageUniqueHitRanking[1])
        Assert.assertEquals("/users.html", spruceProductionLogParser.pageUniqueHitRanking[2])
    }

    @Test
    fun testGetPageRankingByNumOfUniqueUsers() {
        val spruceProductionLogParser = SpruceProductionLogParser("src/test/resources/test.csv")
        spruceProductionLogParser.readCsv()
        Assert.assertEquals(3, spruceProductionLogParser.pageNumUniqueUsersRanking.size)
        Assert.assertEquals("/index.html", spruceProductionLogParser.pageNumUniqueUsersRanking[0])
        Assert.assertEquals("/index.jsp", spruceProductionLogParser.pageNumUniqueUsersRanking[1])
        Assert.assertEquals("/users.html", spruceProductionLogParser.pageNumUniqueUsersRanking[2])
    }

    @Test
    fun testGetUserRankingByUniquePageViews() {
        val spruceProductionLogParser = SpruceProductionLogParser("src/test/resources/test.csv")
        spruceProductionLogParser.readCsv()
        Assert.assertEquals(5, spruceProductionLogParser.usersUniqueViewRanking.size)
        Assert.assertEquals("user5", spruceProductionLogParser.usersUniqueViewRanking[0])
        Assert.assertEquals("user1", spruceProductionLogParser.usersUniqueViewRanking[1])
        Assert.assertEquals("user4", spruceProductionLogParser.usersUniqueViewRanking[2])
        Assert.assertEquals("user3", spruceProductionLogParser.usersUniqueViewRanking[3])
        Assert.assertEquals("user2", spruceProductionLogParser.usersUniqueViewRanking[4])
        assert(true)
    }

}