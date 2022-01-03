import cz.gopay.api.v3.GPClientException
import cz.gopay.api.v3.impl.apacheclient.HttpClientGPConnector
import cz.gopay.api.v3.model.ErrorElement
import cz.gopay.api.v3.model.common.Currency
import cz.gopay.api.v3.model.payment.PaymentFactory
import cz.gopay.api.v3.model.payment.support.Callback
import cz.gopay.api.v3.model.payment.support.Payer
import cz.gopay.api.v3.model.payment.support.PayerContact
import cz.gopay.api.v3.model.payment.support.PaymentInstrument

const val CLIENT_ID = ""
const val CLIENT_CREDENTIALS = ""
const val ESHOP_ID = -1L

const val RETURN_URL = "https://your.return.url"
const val NOTIFY_URL = "https://your.notify.url"

fun main() {
    val connector = HttpClientGPConnector.build("https://gw.sandbox.gopay.com/api");
    connector.getAppToken(CLIENT_ID, CLIENT_CREDENTIALS)


    val allowedPaymentInstruments = mutableListOf<PaymentInstrument>()
    PaymentInstrument.values().forEach(allowedPaymentInstruments::add) // Adding all instruments

    val allowedSwifts = mutableListOf("GIBACZPX", "KOMBCZPP", "RZBCCZPP", "FIOBCZPP") // Some swifts for bank

    val payerContact = PayerContact() // Payer Contact info
    payerContact.firstName = "Joe"
    payerContact.lastName = "Doe"
    payerContact.email = "test@test.cz"
    payerContact.phoneNumber = "+420777456123"
    payerContact.city = "Praha"
    payerContact.street = "Ulice 21"
    payerContact.postalCode = "123 45"
    payerContact.countryCode = "CZE"

    val payer = Payer() // Building payer from above information
    payer.contact = payerContact
    payer.allowedPaymentInstruments = allowedPaymentInstruments
    payer.allowedSwifts = allowedSwifts

    val basePayment = PaymentFactory.createBasePaymentBuilder() // Creating payment with required values + above payer
        .order("1", 2500, Currency.CZK, "Desc")
        .addItem("Item", 1, 1)
        .addAdditionalParameter("AdditionalKey", "AdditionalValue")
        .withCallback(Callback.of(RETURN_URL, NOTIFY_URL))
        .payer(payer)
        .inLang("EN")
        .toEshop(ESHOP_ID)
        .build()
    println("== Base Payment ==")
    println(basePayment.toString())

    try {
        val result = connector.createPayment(basePayment)
        println("== Result ==")
        println(result.toString())
        println(result.gwUrl) // GW URL for testing
    } catch (e: GPClientException) {
        for (err: ErrorElement in e.error.errorMessages) {
            println("Error code: ${err.errorCode}")
            println("Message: ${err.message}")
            println("Field: ${err.field}")
        }
    }
}