package com.kerooker.simpleproperties.internal.profile

import io.kotlintest.extensions.system.withEnvironment
import io.kotlintest.extensions.system.withSystemProperty
import io.kotlintest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec

class SystemProfileLoaderTest : FunSpec() {
    
    init {
        
        test("Should return no profiles if nothing is present in System Environment or Properties") {
            loadProfiles() shouldBe emptyList()
        }
        
        test("Should return a profile if one profile is present in System Environment") {
            withEnvironment("active_profiles", "prod") {
                loadProfiles() shouldBe listOf("prod")
            }
        }
        
        test("Should return multiple profiles if more than one profile is present in System Environment") {
            withEnvironment("active_profiles", "prod,qa,dev") {
                loadProfiles() shouldContainExactlyInAnyOrder listOf("prod", "qa", "dev")
            }
        }
        
        test("Should return the profiles in the order they were declared in the System Environment") {
            withEnvironment("active_profiles", "b,c,a") {
                loadProfiles() shouldBe listOf("b", "c", "a")
            }
        }
        
        test("Should a profile if one profile is presentn in System Properties") {
            withSystemProperty("active_profiles", "prod") {
                loadProfiles() shouldBe listOf("prod")
            }
        }
        
        test("Should return multiple profiles if more than one profile is present in System Properties") {
            withSystemProperty("active_profiles", "prod,qa,dev") {
                loadProfiles() shouldContainExactlyInAnyOrder listOf("prod", "qa", "dev")
            }
        }
        
        test("Should return the profiles in the order they were declared in the System Properties") {
            withSystemProperty("active_profiles", "b,c,a") {
                loadProfiles() shouldBe listOf("b", "c", "a")
            }
        }
        
        test("Should give priority to System Environment if profiles are present in both") {
            withEnvironment("active_profiles", "prod,dev,qa") {
                withSystemProperty("active_profiles", "a,b,c") {
                    loadProfiles() shouldContainExactlyInAnyOrder listOf("prod", "dev", "qa")
                }
            }
        }
    }
    
    private fun loadProfiles() = SystemProfileLoader().loadProfiles()
    
}