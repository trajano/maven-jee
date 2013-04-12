package net.trajano.nosql.mongodb.scala.test

import java.util.Date
import java.util.UUID
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.mongo.tests.MongodForTestsFactory
import net.trajano.maven_jee6.test.LogUtil
import net.trajano.nosql.Customer
import net.trajano.nosql.internal.MongoDbCustomers
import net.trajano.nosql.mongodb.test.CdiProducer
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CustomersTest extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  var testsFactory: MongodForTestsFactory = null

  override def beforeAll = {
    LogUtil.loadConfiguration()
    testsFactory = MongodForTestsFactory.`with`(Version.Main.V2_0)
  }

  override def afterAll = {
    testsFactory.shutdown()
  }

  describe("Customers DAO") {
    it("should be able to create an object") {
      val customer = new Customer
      customer.setName("Archimedes Trajano")
      customer.setLastRecallTimestamp(new Date())
      customer.setUuid(UUID.randomUUID())
    }

    it("should find something that was added") {
      val producer = new CdiProducer()
      val mongo = testsFactory.newMongo()
      val db = mongo.getDB(UUID.randomUUID().toString)
      val customers = new MongoDbCustomers(db)

      val customer = new Customer
      customer.setName("Archimedes Trajano")
      customer.setLastRecallTimestamp(new Date())
      customer.setUuid(UUID.randomUUID())

      customers.add(customer)

      val customerList = customers.find("TRAJANO")
      assert(customerList.size() == 1)
      assert(customerList.get(0).getName() == "Archimedes Trajano")
    }
  }
}