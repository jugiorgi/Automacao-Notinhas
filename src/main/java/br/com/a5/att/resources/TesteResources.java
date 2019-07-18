package br.com.a5.att.resources;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.a5.att.domain.Notinha;
import br.com.a5.att.domain.Produto;


@RestController
@RequestMapping("/resource")
public class TesteResources {

	@CrossOrigin
	@PostMapping
	public String testa(@RequestBody String valor) throws InterruptedException {
				
		System.out.println("URL NOTINHA : " + valor);

		List<Produto> lista = new ArrayList<Produto>();
		Notinha notinha = new Notinha();
		
		System.setProperty("webdriver.chrome.driver", "lib\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		
		try {
			

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS); 
		
		driver.get(valor);

		new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\'linhaTotal\']/span")));

		WebElement count = driver.findElement(By.xpath("//*[@id=\'linhaTotal\']/span"));
		WebElement coletaNomeMercado = driver.findElement(By.xpath("//*[@id=\"u20\"]"));
		String nomeMercado = coletaNomeMercado.getText();
		notinha.setNome(nomeMercado);

		WebElement coletacnpj = driver.findElement(By.xpath("//*[@id=\"conteudo\"]/div[2]/div[2]"));
		String doc = coletacnpj.getText();
		String cnpjV[] = doc.split(":");
		String cnpj = cnpjV[1].replace(" ", "");
		cnpj = cnpj.replace(".", "");
		cnpj = cnpj.replace("/", "");
		cnpj = cnpj.replace("-", "");
		notinha.setCnpj(cnpj);
		
		boolean respostaMercado = ehMercado(cnpj);
	    notinha.setEhMercado(respostaMercado);

		WebElement coletaendereco = driver.findElement(By.xpath("//*[@id=\"conteudo\"]/div[2]/div[3]"));
		String endereco = coletaendereco.getText();
		notinha.setEndereco(endereco);

		int qnt = Integer.parseInt(count.getText());
		notinha.setQntProdutos(qnt);

		WebElement coletadata = driver.findElement(By.xpath("//*[@id=\"ctl00\"]/div[3]/div[1]/div[1]/span"));
		String dataText = coletadata.getText();
		String dataV[] = dataText.split(" ");
		String data = dataV[1];
		data = data.replace("/", "-");
		
		String dataValores[] = data.split("-");
		String novaData = dataValores[2] + "-" + dataValores[1] + "-" + dataValores[0];
		notinha.setData(novaData);

		for (int i = 1; i <= qnt; i++) {
			new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.id("Item + " + i)));

			WebElement input = driver.findElement(By.id("Item + " + i));
			String str = input.getText();

			String parse = str.replace("\r\n", "");
			String nome = parse.substring(0, parse.indexOf("(") - 1);
			String parseCodigo = parse.substring(parse.indexOf("(") + 1, parse.indexOf(")") - 1);
			parseCodigo = parseCodigo.replace("Código: ", "");
			String tipoProduto = parse.substring(parse.indexOf("UN: ") + 4);
			tipoProduto = tipoProduto.substring(0, 2);
			String parseValor = parse.substring(parse.indexOf("Unit.:"));
			parseValor = parseValor.replace("Unit.:", "").trim();
			parseValor = parseValor.substring(0, parseValor.indexOf(" "));

			Produto produto = new Produto();

			produto.setNome(nome);
			produto.setCodigo(parseCodigo);
			produto.setUnidade(tipoProduto);
			parseValor = parseValor.replace(",", ".");
			produto.setValor(parseValor);

			lista.add(produto);

		}
		
		driver.close();

		notinha.setListaProdutos(lista);
		notinha.setExcecao(false);
		
		
		} catch (Exception e) {
			notinha.setExcecao(true);
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.out.println(e.getCause());
		}
		
		if(notinha.isExcecao()) {
			driver.close();
		}
			
		return notinha.toString();
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
