package com.example

import io.ktor.http.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.example.plugins.*

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }

    @Test
    fun `test - Get example input (M1 + M2)`() = testApplication {
        client.get("/example?type=float").apply {
            val respose = "{\"argsList\":[{\"rows\":2,\"columns\":2,\"type\":1,\"matrix\":[[1.0,0.0],[0.0,1.0]]},{\"rows\":2,\"columns\":2,\"type\":1,\"matrix\":[[1.0,2.0],[3.0,4.0]]}],\"rootExpr\":{\"operation\":\"MINUS\",\"leftTableIndex\":0,\"rightTableIndex\":1}}"
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(respose, bodyAsText())
        }
    }

    @Test
    fun `test - Ex M1 + M1`() = testApplication {
        val response = client.post("/expression?type=float") {
            contentType(ContentType.Application.Json)
            setBody("{ \"argsList\": [ { \"rows\": 2, \"columns\": 2, \"type\": 1, \"matrix\": [ [ 1.0, 0.0 ], [ 0.0, 1.0 ] ] }, { \"rows\": 2, \"columns\": 2, \"type\": 1, \"matrix\": [ [ 1.0, 2.0 ], [ 3.0, 4.0 ] ] } ], \"rootExpr\": { \"operation\": \"MINUS\", \"leftTableIndex\": 0, \"rightTableIndex\": 1 } }")
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals("0", response.bodyAsText())

        //check result
        client.get("/expression?id=0&type=float").apply {
            val respose = "[     0,00    -2,00  ]\n" +
                    "[    -3,00    -3,00  ]\n"
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(respose, bodyAsText())
        }
    }

    @Test
    fun `test - Ex inverse(M1)`() = testApplication {
        val response = client.post("/expression?type=float") {
            contentType(ContentType.Application.Json)
            setBody("{ \"argsList\": [ { \"rows\": 2, \"columns\": 2, \"type\": 1, \"matrix\": [ [ 1.0, 2.0 ], [ 3.0, 4.0 ] ] } ], \"rootExpr\": { \"operation\": \"INVERSE\", \"leftTableIndex\": 0 } }")
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals("0", response.bodyAsText())

        //check result
        client.get("/expression?id=0&type=float").apply {
            val respose = "[    -2,00     1,00  ]\n" +
                    "[     1,50    -0,50  ]\n"
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(respose, bodyAsText())
        }
    }

    @Test
    fun `test - Ex transpose(M1)`() = testApplication {
        val response = client.post("/expression?type=float") {
            contentType(ContentType.Application.Json)
            setBody("{\"argsList\":[{\"rows\":2,\"columns\":2,\"type\":1,\"matrix\":[[1.0,2.0],[3.0,4.0]]}],\"rootExpr\":{\"operation\":\"TRANSPOSE\",\"leftTableIndex\":0}}")
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals("0", response.bodyAsText())

        //check result
        client.get("/expression?id=0&type=float").apply {
            val respose = "[     1,00     3,00  ]\n" +
                    "[     2,00     4,00  ]\n"
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(respose, bodyAsText())
        }
    }

    @Test
    fun `test - Ex M1 times M2`() = testApplication {
        val response = client.post("/expression?type=float") {
            contentType(ContentType.Application.Json)
            setBody("{ \"argsList\": [ { \"rows\": 2, \"columns\": 2, \"type\": 1, \"matrix\": [ [ 1.0, 0.0 ], [ 0.0, 1.0 ] ] }, { \"rows\": 2, \"columns\": 2, \"type\": 1, \"matrix\": [ [ 1.0, 2.0 ], [ 3.0, 4.0 ] ] } ], \"rootExpr\": { \"operation\": \"TIMES\", \"leftTableIndex\": 0, \"rightTableIndex\": 1 } }")
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals("0", response.bodyAsText())

        //check result
        client.get("/expression?id=0&type=float").apply {
            val respose = "[     1,00     2,00  ]\n" +
                    "[     3,00     4,00  ]\n"
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(respose, bodyAsText())
        }
    }

}