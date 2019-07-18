package br.com.a5.att.resources;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class print {

	public static void main(String[] args) {
		
		String mercado = "93.015.006/0001-13";
		
		
		mercado = mercado.replace(".", "");
		mercado = mercado.replace("/", "");
		mercado = mercado.replace("-", "");
		
		
		ehMercado(mercado);
		
		
		
		
//		Scanner entrada = new Scanner(System.in);
//
//		try {
//			String url = "https://satsp.fazenda.sp.gov.br/COMSAT/Public/ConsultaPublica/ConsultaPublicaCfe.aspx";
//			//String url = "https://www.google.com";
//			String chaveAcesso = "35190524714083000149590006203410171636815066";
//			String novaChave = "";
//
//			novaChave = editaChaveAcesso(chaveAcesso);
//			
//			
//			
//			System.setProperty("webdriver.chrome.driver", "lib\\chromedriver.exe");
//			WebDriver driver = new ChromeDriver();
//			driver.manage().window().maximize();
//
//			driver.get(url);
//			
//
//			Robot robot = new Robot();
//			BufferedImage bi = robot.createScreenCapture(new Rectangle(475, 345, 150, 55));
//			ImageIO.write(bi, "jpg", new File("img\\imageTest.jpg"));
//			
//			
//			
//			System.out.println("Digite o captcha: ");
//			String captcha = entrada.nextLine();
//			
//			
//			WebElement chave = driver.findElement(By.xpath("//*[@id=\"conteudo_txtChaveAcesso\"]"));
//			chave.sendKeys(novaChave);
//			
//			WebElement captchaElement = driver.findElement(By.xpath("//*[@id=\"conteudo_txtCaptcha\"]"));
//			captchaElement.sendKeys(captcha);
//			
//			WebElement consultar = driver.findElement(By.xpath("//*[@id=\"conteudo_btnConsultar\"]"));
//			consultar.click();
//			
//			driver.close();
//			
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println(e.getCause());
//			System.out.println(e.getMessage());
//		}
	}
	
	public static String editaChaveAcesso(String chaveAcesso) {
		String novaChave = "";
		char ultimo;
		
		ultimo = chaveAcesso.charAt(43);
		novaChave = ultimo + chaveAcesso;
		
		return novaChave;
	}
	
	public static boolean ehMercado(String cnpj) {
		
		
		
		boolean resposta = false;
		
		List<String> mercados = new ArrayList<String>();
		
		mercados.add("4729602"); //Comércio varejista de mercadorias em lojas de conveniência
		mercados.add("4789099"); //Comércio varejista de outros produtos não especificados anteriormente
		mercados.add("4711301"); //Comércio varejista de mercadorias em geral, com predominância de produtos alimentícios - hipermercados 
		mercados.add("4711302"); //Comércio varejista de mercadorias em geral, com predominância de produtos alimentícios - supermercados 
		mercados.add("4712100"); //Comércio varejista de mercadorias em geral, com predominância de produtos alimentícios - minimercados, mercearias e armazéns
		mercados.add("4633801"); //Comércio atacadista de frutas, verduras, raízes, tubérculos, hortaliças e legumes frescos
		
		List<String> codigos = new ArrayList<String>();
		
		try {
			
			try {
				JSONObject json = new JSONObject();

	            URL url = new URL("https://www.receitaws.com.br/v1/cnpj/" + cnpj);
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setRequestMethod("GET");
	            conn.setRequestProperty("Accept", "application/json");
	            if (conn.getResponseCode() != 200) {
	                throw new RuntimeException("Failed : HTTP Error code : " + conn.getResponseCode());
	            }
	            InputStreamReader in = new InputStreamReader(conn.getInputStream());
	            BufferedReader br = new BufferedReader(in);
	            StringBuffer response = new StringBuffer();
	            String output;
	            while ((output = br.readLine()) != null) {
	            	response.append(output);
	            }
	            
	            JSONObject object = new JSONObject(response.toString()); 
	            System.out.println(object.toString());
	            System.out.println("NOME: " + object.get("nome"));
	            
	            
	            JSONArray array_principal = object.getJSONArray("atividade_principal");
	            String principal = array_principal.getString(0);
	            JSONObject obj_principal = new JSONObject(principal);
	            String p = obj_principal.getString("code");
	            p = p.replace(".", "");
	            p = p.replace("-", "");
	            codigos.add(p);
	            
	            
	            JSONArray array_secundario = object.getJSONArray("atividades_secundarias");
	            for(int i = 0; i < array_secundario.length(); i++) {
	            	String secundario = array_secundario.getString(i);
	            	JSONObject obg_secundario = new JSONObject(secundario);
		            String s = obg_secundario.getString("code");
		            s = s.replace(".", "");
		            s = s.replace("-", "");
		            codigos.add(s);
	            }
	            
	            conn.disconnect();

	        } catch (Exception e) {
	            System.out.println("Exception in NetClientGet:- " + e);
	        }
			
			

			
			int tem = 0;
			int nTem = 0;
			
			for(int k = 0; k < mercados.size(); k++) {
			
				for(int i = 0; i < codigos.size(); i++) {
						if(mercados.get(k).equals(codigos.get(i))) {
							tem++;
						}else {
							nTem++;
						}
				}
			}
			
			if(tem > 0) {
				resposta = true;
			}else {
				resposta = false;
			}
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			e.printStackTrace();
		}
		
		System.out.println("mercados: " + mercados.toString());
		System.out.println("codigos: " + codigos.toString());
		System.out.println(resposta);
		return resposta;
	}
	}


