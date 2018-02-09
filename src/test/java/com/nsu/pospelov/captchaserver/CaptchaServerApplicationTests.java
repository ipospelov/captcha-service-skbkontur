package com.nsu.pospelov.captchaserver;

import com.nsu.pospelov.captchaserver.captcha_generator.CaptchaService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
public class CaptchaServerApplicationTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;


    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    /*Проверка правильности формата передаваемых данных (jpeg изображение)*/
	@Test
	public void contextTypeTest() throws Exception{
        this.mockMvc.perform(get("/").accept(MediaType.IMAGE_JPEG))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.IMAGE_JPEG));
	}


	/*Ожидаемое поведение в ответ на передачу ответа с несуществующим ID запроса - ошибка 418*/
	@Test
	public void testNonExistsRequestID() throws Exception{

        this.mockMvc.perform(MockMvcRequestBuilders.post("/")
                .param("request_id","random_value")
                .param("captcha_key","random_value"))
        .andExpect(status().is(418));

    }

    /*Ожидаемое поведение в ответ на неправильно сформированный запрос (отсутствуют поля с ID запроса и ответ на капчу) - ошибка 400*/
    @Test
    public void testBadRequest() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.post("/"))
                .andExpect(status().is(400));
    }

    /*Ожидаемое поведение в ответ на верно сформированный запрос с правильно заполненными полями - код 200 и "Success" в теле ответ*/
    @Test
    public void testCorrectRequest() throws Exception{

        HttpServletResponse response = this.mockMvc.perform(get("/")).andReturn().getResponse();
        String requestID = response.getHeader("request_id");
        String captchaKey = response.getHeader("captcha_key");

        this.mockMvc.perform(MockMvcRequestBuilders.post("/")
                .param("request_id",requestID)
                .param("captcha_key",captchaKey))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success")));

    }

    /*Ожидаемое поведение на несвоевременный (на ответ дается 5 секунд) правильный ответ - ошибка 418 и "Error" в теле ответа*/
    @Test
    public void testCorrectAfterPauseRequest() throws Exception{

        HttpServletResponse response = this.mockMvc.perform(get("/")).andReturn().getResponse();
        String requestID = response.getHeader("request_id");
        String captchaKey = response.getHeader("captcha_key");

        Thread.sleep(5500);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/")
                .param("request_id",requestID)
                .param("captcha_key",captchaKey))
                .andExpect(status().is(418))
                .andExpect(content().string(Matchers.containsString("Error")));

    }

    /*Ожидаемое поведение на попытку ответить несколько раз на одну капчу - ошибка 418 и "Error" в теле ответа*/
    @Test
    public void testAfterFailedTry() throws Exception{

        HttpServletResponse response = this.mockMvc.perform(get("/")).andReturn().getResponse();
        String requestID = response.getHeader("request_id");
        String captchaKey = response.getHeader("captcha_key");

        this.mockMvc.perform(MockMvcRequestBuilders.post("/")
                .param("request_id",requestID)
                .param("captcha_key","badKey"))
                .andExpect(status().is(418))
                .andExpect(content().string(Matchers.containsString("Error")));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/")
                .param("request_id",requestID)
                .param("captcha_key",captchaKey)) //правильный ответ
                .andExpect(status().is(418))
                .andExpect(content().string(Matchers.containsString("Error")));
    }

    /*Проверка правильности обработки ответов в порядке, не соответствующем изначальному порядку следования запросов от пользователей*/
    @Test
    public void testSwapedResponses() throws Exception{

        HttpServletResponse response = this.mockMvc.perform(get("/")).andReturn().getResponse();
        String requestID = response.getHeader("request_id");
        String captchaKey = response.getHeader("captcha_key");

        response = this.mockMvc.perform(get("/")).andReturn().getResponse();
        String requestID2 = response.getHeader("request_id");
        String captchaKey2 = response.getHeader("captcha_key");

        this.mockMvc.perform(MockMvcRequestBuilders.post("/")
                .param("request_id",requestID2)
                .param("captcha_key",captchaKey2))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success")));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/")
                .param("request_id",requestID)
                .param("captcha_key",captchaKey))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success")));
    }

    @Test
    public void testSwapedResponses2() throws Exception{

        HttpServletResponse response = this.mockMvc.perform(get("/")).andReturn().getResponse();
        String requestID = response.getHeader("request_id");
        String captchaKey = response.getHeader("captcha_key");

        response = this.mockMvc.perform(get("/")).andReturn().getResponse();
        String requestID2 = response.getHeader("request_id");
        String captchaKey2 = response.getHeader("captcha_key");

        this.mockMvc.perform(MockMvcRequestBuilders.post("/")
                .param("request_id",requestID)
                .param("captcha_key",captchaKey))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success")));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/")
                .param("request_id",requestID2)
                .param("captcha_key",captchaKey2))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success")));
    }

    @Test
    public void testCorrectAfterFail() throws  Exception{

        HttpServletResponse response = this.mockMvc.perform(get("/")).andReturn().getResponse();
        String requestID = response.getHeader("request_id");

        response = this.mockMvc.perform(get("/")).andReturn().getResponse();
        String requestID2 = response.getHeader("request_id");
        String captchaKey2 = response.getHeader("captcha_key");

        this.mockMvc.perform(MockMvcRequestBuilders.post("/")
                .param("request_id",requestID)
                .param("captcha_key","random"))
                .andExpect(status().is(418))
                .andExpect(content().string(Matchers.containsString("Error")));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/")
                .param("request_id",requestID2)
                .param("captcha_key",captchaKey2))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success")));

    }

    @Test
    public void testBadRequest2() throws Exception{


        HttpServletResponse response = this.mockMvc.perform(get("/")).andReturn().getResponse();
        String captchaKey = response.getHeader("captcha_key");

        this.mockMvc.perform(MockMvcRequestBuilders.post("/")
                .param("captcha_key",captchaKey))
                .andExpect(status().is(400));



    }

    @Test
    public void testBadRequest3() throws Exception{


        HttpServletResponse response = this.mockMvc.perform(get("/")).andReturn().getResponse();
        String requestID = response.getHeader("request_id");


        this.mockMvc.perform(MockMvcRequestBuilders.post("/")
                .param("request_id",requestID))
                .andExpect(status().is(400));

    }

    @Test
    public void testMultiplyAccess() throws Exception{

        HttpServletResponse response = this.mockMvc.perform(get("/")).andReturn().getResponse();
        String requestID = response.getHeader("request_id");
        String captchaKey = response.getHeader("captcha_key");

        response = this.mockMvc.perform(get("/")).andReturn().getResponse();
        String requestID2 = response.getHeader("request_id");
        String captchaKey2 = response.getHeader("captcha_key");

        response = this.mockMvc.perform(get("/")).andReturn().getResponse();
        String requestID3 = response.getHeader("request_id");
        String captchaKey3 = response.getHeader("captcha_key");

        this.mockMvc.perform(MockMvcRequestBuilders.post("/")
                .param("request_id",requestID2)
                .param("captcha_key",captchaKey2))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success")));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/")
                .param("request_id",requestID)
                .param("captcha_key",captchaKey))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success")));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/")
                .param("request_id",requestID3)
                .param("captcha_key",captchaKey3))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success")));

    }

    @Test
    public void testMulriplyAccessWithTimeOut() throws Exception{

        HttpServletResponse response = this.mockMvc.perform(get("/")).andReturn().getResponse();
        String requestID = response.getHeader("request_id");
        String captchaKey = response.getHeader("captcha_key");

        response = this.mockMvc.perform(get("/")).andReturn().getResponse();
        String requestID2 = response.getHeader("request_id");
        String captchaKey2 = response.getHeader("captcha_key");

        this.mockMvc.perform(MockMvcRequestBuilders.post("/")
                .param("request_id",requestID2)
                .param("captcha_key",captchaKey2))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Success")));

        Thread.sleep(5500);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/")
                .param("request_id",requestID)
                .param("captcha_key",captchaKey))
                .andExpect(status().is(418))
                .andExpect(content().string(Matchers.containsString("Error")));

    }


}
