//package com.barobaro.app.service.impl;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.stereotype.Component;
////import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.stereotype.Component;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import com.barobaro.app.common.CommonCode.UserInfo;
//import com.barobaro.app.service.Oauth;
//
//import java.io.BufferedOutputStream;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//
//@Component
//@RequiredArgsConstructor
//public class GoogleOauth implements Oauth {
////	@Value("${google.loginform.url}")
//	private String LOGIN_FORM_URL;
////	@Value("${google.client.id}")
//	private String CLIENT_ID;
////	@Value("${google.client.pw}")
//	private String CLIENT_PW;
////	@Value("${google.redirect.uri}")
//	private String CALLBACK_URL;
////	@Value("${google.endpoint.token}")
//	private String ENDPOINT_URL_TOKEN;
////	@Value("${google.endpoint.userinfo}")
//	private String ENDPOINT_URL_USERINFO;
//	private String ACCESS_TOKEN  = "";
//    
//	//	public static String commonBuildQueryString(Map<String, Object> params) {
//	//		StringBuilder queryString = new StringBuilder();
//	//		try {
//	//			for (Map.Entry<String, Object> entry : params.entrySet()) {
//	//				if (queryString.length() > 0) {
//	//					queryString.append("&");
//	//				}
//	//				queryString.append(URLEncoder.encode(entry.getKey(), "UTF-8"))
//	//				.append("=")
//	//				.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
//	//			}
//	//		} catch(UnsupportedEncodingException e) {
//	//			e.printStackTrace();
//	//		}
//	//		return queryString.toString();
//	//	}
//
//	/** 
//	 * 구글의 로그인창 주소
//	 */
//	@Override
//	public String getLoginFormURL() {
//		Map<String, Object> params = new HashMap<>();
//		params.put("scope"			, "email%20profile%20openid");
//		params.put("response_type"	, "code");
//		params.put("client_id"		, CLIENT_ID);
//		params.put("redirect_uri"	, CALLBACK_URL);
//		params.put("access_type"	, "offline");		
//		params.put("prompt"			, "consent");
//
//		//String parameterString = commonBuildQueryString(params);
//		String parameterString = params.entrySet().stream()
//				.map(x -> x.getKey() + "=" + x.getValue())
//				.collect(Collectors.joining("&"));  
//		
//		System.out.println("GoogleOauth : "+ parameterString);
//		//https://accounts.google.com/o/oauth2/v2/auth?client_id=__&redirect_uri=__&response_type=code&scope=email profile openid&access_type=offline
//		return LOGIN_FORM_URL + "?" + parameterString;
//	}
//
//	/** 
//	 * AccessToken 받기
//	 */
//	@Override
//	public Map<String, String> requestAccessToken(String code) {
//		Map<String, String> bodys = new HashMap<String, String>();
//		bodys.put("code"			, code);
//		bodys.put("client_id"		, CLIENT_ID);
//		bodys.put("client_secret"	, CLIENT_PW);
//		bodys.put("redirect_uri"	, CALLBACK_URL);
//		bodys.put("grant_type"		, "authorization_code");
//
//		//--------------------------------------------------			
//		//방법1) RestTemplate Map 바인딩 + 키로 토큰만 꺼내기
//		//--------------------------------------------------
//		HttpEntity<Map<String, String>> entity = new HttpEntity<>(bodys);
//		
//		System.out.println("GoogleOauth requestAccessToken : ");
//		
//		RestTemplate restTemplate = new RestTemplate();
//		
//		System.out.println("여기냐?1");
//		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter()); // Jackson Converter 추가
//		
//		ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(
//		    ENDPOINT_URL_TOKEN,
//		    HttpMethod.POST,
//		    entity,
//		    new ParameterizedTypeReference<Map<String, String>>() {}
//		);		
//		
//		
//		System.out.println("GoogleOauth requestAccessToken 2: ");
//		this.ACCESS_TOKEN = (String)responseEntity.getBody().get("access_token");
//		
//		
//		System.out.println("GoogleOauth.requestAccessToken3() accessToken:"+ this.ACCESS_TOKEN);
//		if (responseEntity.getStatusCode() == HttpStatus.OK) 
//			System.out.println(responseEntity.getBody().toString());
//		
//		//			--------------------------------------------------			
//		//			방법2) GoogleRequestVo  Builder + Entity에 담아서 꺼내기
//		//			--------------------------------------------------			
//		//			RestTemplate restTemplate = new RestTemplate();
//		//			GoogleRequest googleRequest = GoogleRequest
//		//					.builder()
//		//					.clientId(CLIENT_ID)
//		//					.clientSecret(CLIENT_PW)
//		//					.code(code)
//		//					.redirectUri(CALLBACK_URL)
//		//					.grantType("authorization_code").build();
//		//			System.out.println(googleRequest.toString());
//		//			ResponseEntity<GoogleResponse> googleResponse = restTemplate.postForEntity("https://oauth2.googleapis.com/token", googleRequest, GoogleResponse.class);
//		//			System.out.println(googleResponse.toString());
//
//		//			--------------------------------------------------
//		//			방법3) commonBuildQueryString() 쿼리스트링 만들기
//		//			--------------------------------------------------
//		//			LOGIN_FORM_URL + "?" + parameterString;
//		return responseEntity.getBody();
//	}
//
//	/** 
//	 * AccessToken을 사용해 유저정보 받기
//	 */
//	public Map<String, String> getUserInfo(String accessToken) {
//		
//		 //필수 헤더 정보
//		HttpHeaders headers = new HttpHeaders();
//		headers.set("Authorization", "Bearer " + this.ACCESS_TOKEN);
//		HttpEntity<Map<String, String>> entity = new HttpEntity<>(headers);
//		RestTemplate restTemplate = new RestTemplate();
//		ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(
//				ENDPOINT_URL_USERINFO,
//			    HttpMethod.GET,
//			    entity,
//			    new ParameterizedTypeReference<Map<String, String>>() {}
//			);		
//		System.out.println("4.유저정보 응답:" +responseEntity.getBody().toString());    
//		
//		return responseEntity.getBody();
//	}
//
//	/** 
//	 * refreshToken을 사용해 AccessToken 재발급 받기
//	 */
//	public String getAccessTokenByRefreshToken(String refreshToken) {
//		Map<String, String> bodys = new HashMap<>();
//		bodys.put("client_id"     , CLIENT_ID);
//		bodys.put("client_secret" , CLIENT_PW);
//		bodys.put("refresh_token" , refreshToken);
//		bodys.put("grant_type"    , "refresh_token");
//        
//        // HttpEntity (바디) 생성
//		RestTemplate restTemplate = new RestTemplate();
//		HttpEntity<Map<String, String>> entity = new HttpEntity<>(bodys);
//        ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(
//        		ENDPOINT_URL_TOKEN,
//			    HttpMethod.POST,
//			    entity,
//			    new ParameterizedTypeReference<Map<String, String>>() {}
//			);
//        System.out.println("4.토큰재발급 응답(body):" + responseEntity.getBody().toString());
//        String accessToken= responseEntity.getBody().get("access_token");
//        System.out.println("4.토큰요청 응답(갱신된access_token): " + accessToken);
//        return accessToken;
//	}
//
//	/** 
//	 * AccessToken 만료 여부 체크
//	 */
//	public boolean isTokenExpired(String accessToken) {
//
//		try {
//			HttpHeaders headers = new HttpHeaders();
//			headers.set("Authorization", "Bearer " + accessToken);
//			HttpEntity<String> entity = new HttpEntity<>(headers);
//			
//			RestTemplate restTemplate = new RestTemplate();
//			ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(
//					ENDPOINT_URL_USERINFO,
//				    HttpMethod.POST,
//				    entity,
//				    new ParameterizedTypeReference<Map<String, String>>() {}
//				);
//			System.out.println("5.유저정보 응답:" + responseEntity.getBody().toString());
//			return false; // 요청 성공 -> 토큰 유효
//		} catch (HttpClientErrorException e) {
//			if (e.getStatusCode().value() == 401) {
//				return true; // 401 Unauthorized -> 토큰 만료
//			}
//			throw e; // 다른 오류는 예외로 처리
//		}
//	}
//
//	@Override
//	public UserInfo getUserInfo2(String accessToken) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//
//
//
//}