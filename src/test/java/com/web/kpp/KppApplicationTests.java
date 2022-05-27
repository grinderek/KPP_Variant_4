package com.web.kpp;

import com.web.kpp.entity.TriangleIdentification;
import com.web.kpp.exceptions.TriangleDoesNotExistException;

import com.web.kpp.service.TriangleIdentificationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KppApplicationTests {
  @Autowired private TriangleIdentificationService triangleIdentificationService;

  @Autowired private WebApplicationContext context;

  private MockMvc mockMvc;

  @Before
  public void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
  }

  @Test
  public void givenWac_whenServletContext_thenItProvidesController() {
    ServletContext servletContext = context.getServletContext();

    Assert.assertNotNull(servletContext);
    Assert.assertTrue(servletContext instanceof MockServletContext);
    Assert.assertNotNull(context.getBean("triangleIdentificationController"));
  }

  @Test
  public void triangleIdentification_successfulResult_correctRequest() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(get("/identification/?side1=3&side2=4&side3=5"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.isRectangular").value(true))
            .andExpect(jsonPath("$.isEquilateral").value(false))
            .andExpect(jsonPath("$.isIsosceles").value(false))
            .andReturn();

    Assert.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
  }

  @Test
  public void triangleIdentification_badRequest_wrongParamName() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(get("/identification/?wrongName=3&side2=4&side3=5"))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "Required request parameter 'side1' for method parameter type int is not present"))
            .andReturn();

    Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
  }

  @Test
  public void triangleIdentification_badRequest_negativeNumber() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(get("/identification/?side1=-3&side2=4&side3=5"))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("triangleParams.side1: Value must be positive!"))
            .andReturn();

    Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
  }

  @Test
  public void triangleIdentification_successfulResult_correctRequest_2() {
    int side1 = 3;
    int side2 = 4;
    int side3 = 5;

    TriangleIdentification actual = triangleIdentificationService.identify(side1, side2, side3);
    assertTrue(actual.getIsRectangular());
    assertFalse(actual.getIsEquilateral());
    assertFalse(actual.getIsIsosceles());
  }

  @Test
  public void triangleIdentification_badRequest_triangleDoesNotExist() {
    int side1 = 100;
    int side2 = 4;
    int side3 = 5;
    TriangleDoesNotExistException thrown =
        assertThrows(
            TriangleDoesNotExistException.class,
            () -> {
              triangleIdentificationService.identify(side1, side2, side3);
            });

    String expectedMessage = "Triangle does not exist!";
    String actualMessage = thrown.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }
}
