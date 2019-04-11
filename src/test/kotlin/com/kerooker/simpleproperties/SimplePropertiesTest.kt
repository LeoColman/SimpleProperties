package com.kerooker.simpleproperties

import io.kotlintest.extensions.system.withEnvironment
import io.kotlintest.extensions.system.withSystemProperty
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.shouldThrowAny
import io.kotlintest.specs.FunSpec

class SimplePropertiesTest : FunSpec() {

    init {
        test("Should load default properties from default location if no profile is set") {
            val props = SimpleProperties()
            
            props["default-from-default"] shouldBe "DefaultFromDefault"
        }
        
        test("Should throw exception when key doesnt exist") {
            val props = SimpleProperties()
            
            val exception = shouldThrow<PropertyNotDefinedException> { props["inexistent-key"] }
            
            exception.message shouldBe "Property <inexistent-key> is not defined. Use simpleProperty.getOptional(\"inexistent-key\") instead"
        }
        
        test("Should return property if it is present and loaded optionally") {
            val props = SimpleProperties()
            
            props.getOptional("default-from-default") shouldBe "DefaultFromDefault"
        }
        
        test("Should not throw exception if property is not present and loaded optionally") {
            val props = SimpleProperties()
            
            props.getOptional("inexistent-key") shouldBe null
        }
        
        test("Should load values from both default and profile") {
            val props = SimpleProperties(profiles = listOf("prod"))
            
            props["default-from-default"] shouldBe "DefaultFromDefault"
            props["prod-from-default"] shouldBe "ProdFromDefault"
        }
        
        test("Should override values from default if present in profile") {
            val props = SimpleProperties(profiles = listOf("default-override"))
            
            props["default-from-default"] shouldBe "Overriden"
        }
        
        test("Should load profiles from environment") {
            withEnvironment("active_profiles","prod") {
                val props = SimpleProperties()
                
                props["prod-from-default"] shouldBe "ProdFromDefault"
            }
        }
        
        test("Should load profiles from properties") {
            withSystemProperty("active_profiles", "prod") {
                val props = SimpleProperties()
                props["prod-from-default"] shouldBe "ProdFromDefault"
            }
        }
        
        test("Should allow changing default profile location") {
            val props = SimpleProperties(filesLocation = "non_default")
            
            props["default-from-non-default"] shouldBe "DefaultFromNonDefault"
        }
        
        test("Should load profile, even if there is no application.properties file") {
            val props = SimpleProperties(filesLocation = "without_default", profiles = listOf("qa"))
            
            props["qa-property"] shouldBe "QaProperty"
        }
        
        test("Should throw an exception on initialization if profile does not exist") {
            
            shouldThrowAny { SimpleProperties(listOf("InexistentProfile")) }
        }
        
    }

}