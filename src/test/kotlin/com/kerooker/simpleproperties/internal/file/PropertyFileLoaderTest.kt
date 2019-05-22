package com.kerooker.simpleproperties.internal.file

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.FunSpec

class PropertyFileLoaderTest : FunSpec() {

    init {
        
        test("Should load from relative path if an empty file location is provided") {
            PropertyFileLoader("").loadDefaultFile() shouldBe mapOf(
                "default-from-default" to "DefaultFromDefault",
                "default-from-default2" to "DefaultFromDefault2"
            )
        }
        
        test("Should load default file (application.properties) from default location (root)") {
            PropertyFileLoader().loadDefaultFile() shouldBe mapOf(
                "default-from-default" to "DefaultFromDefault",
                "default-from-default2" to "DefaultFromDefault2"
            )
        }
        
        test("Should load default file (application.properties) from non-default location") {
            PropertyFileLoader(filesLocation = "non_default").loadDefaultFile() shouldBe mapOf(
                "default-from-non-default" to "DefaultFromNonDefault",
                "default-from-non-default2" to "DefaultFromNonDefault2"
            )
        }
        
        test("Should load profile file from default location (root") {
            PropertyFileLoader().loadProfileFile("prod") shouldBe mapOf(
                "prod-from-default" to "ProdFromDefault",
                "prod-from-default2" to "ProdFromDefault2"
            )
        }
        
        test("Should load profile file from non-default location") {
            PropertyFileLoader(filesLocation = "non_default").loadProfileFile("prod") shouldBe mapOf(
                "prod-from-non-default" to "ProdFromNonDefault",
                "prod-from-non-default2" to "ProdFromNonDefault2"
            )
        }
        
        test("Should return empty map if application.properties doesn't exist") {
            PropertyFileLoader(filesLocation = "without_default").loadDefaultFile() shouldBe emptyMap<String, String>()
        }
        
        test("Should throw exception if profile does not exist") {
            val exception = shouldThrow<FailedToLoadProfileException> { PropertyFileLoader("folderLocation").loadProfileFile("inexistent") }
            exception.message shouldBe "Could not load profile <inexistent> from <folderLocation>"
        }
    }

}