import com.mongodb.Mongo
import de.flapdoodle.embedmongo.config.MongodConfig
import de.flapdoodle.embedmongo.distribution.Version
import de.flapdoodle.embedmongo.runtime.Network
import de.flapdoodle.embedmongo.MongoDBRuntime
import java.util.{UUID, Date}
import net.trajano.maven_jee6.test.LogUtil
import net.trajano.nosql.{Customers, Customer}
import net.trajano.nosql.internal.MongoDbCustomers
import net.trajano.nosql.mongodb.test.CdiProducer
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class CustomersTest extends FunSpec with ShouldMatchers {
  describe("Customers DAO") {
    it("should be able to create an object") {
      val customer = new Customer
      customer.setName("Archimedes Trajano")
      customer.setLastRecallTimestamp(new Date())
      customer.setUuid(UUID.randomUUID())
    }
    it("should find something that was added") {
    	  LogUtil.loadConfiguration();
    
      val producer = new CdiProducer() 
      val mongodExecutable = producer.createMongodExecutable(12345);
      val mongoProcess = mongodExecutable.start()
      val mongo = new Mongo("localhost", 12345)
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

      mongoProcess.stop()
      mongodExecutable.cleanup()
    }
  }
}