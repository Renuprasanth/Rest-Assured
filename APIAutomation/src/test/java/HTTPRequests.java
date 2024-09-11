import org.testng.annotations.Test;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import org.json.JSONObject;


/*
 * RestAssured is a Java API/Library which helps in validating the API
 * responses. Kind of BDD which uses given(), when(), then() and and().
 * 
 * given() -->  contentType, setCookie, Auth, parameters, headers, etc...
 * 
 * when()  -->  HTTP methods
 * 
 * then()  -->  Validate status codes, extract responses, extract request body, headers & cookies etc.. 
 */

public class HTTPRequests {
	
	static { 
		baseURI = "https://reqres.in/";
		}
	
	int id;

	@Test(priority = 1)
	public void getUsers() {
		
		when()
		 .get("api/users?page=2")
		.then()
		 .statusCode(200)
		 .body("page", equalTo(2))
		 .body("data[2].email", equalTo("tobias.funke@reqres.in"))
		 .log().all();
	}
	
	@Test(priority = 2)
	public void createUser() {
		
		JSONObject data = new JSONObject();
		data.put("name", "Renu");
		data.put("job", "Tester");
		
	id = given()
		 .contentType(ContentType.JSON)
		 .body(data.toString())
		.when()
		 .post("/api/users")
		 .jsonPath().getInt("id");
	
	System.out.println(id);
	}
	
	@Test(priority = 3, dependsOnMethods = {"createUser"})
	public void fullUpdate() {
		
		JSONObject data = new JSONObject();
		data.put("name", "Renu");
		data.put("job", "Associate Consultant");
		
		given()
		 .contentType(ContentType.JSON)
		 .body(data.toString())
		.when()
		 .put("api/users/"+id)
		.then()
		 .statusCode(200)
		 .log().all();
	}
	
	@Test(priority = 4)
	public void deleteUser() {
		
		when()
		 .delete("/api/users/"+id)
		.then()
		 .statusCode(204)
		 .log().all();
	}
}
