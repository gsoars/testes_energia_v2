package tests;

import model.MedicaoRequest;
import model.OcorrenciaRequest;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;
import java.util.List;
import model.LoginRequest;
import model.LoginResponse;
import services.EnergiaApiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

public class ApiValidationTests {

    private static EnergiaApiService apiService;
    private static String adminToken;
    private static String userToken;
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String SCHEMAS_PATH = "src/test/resources/schemas/";

    @BeforeClass
    public static void setup() {
        apiService = new EnergiaApiService();

        adminToken = apiService.fazerLogin(new LoginRequest("usuario2@email.com", "123456"))
                .as(LoginResponse.class)
                .getToken();

        userToken = apiService.fazerLogin(new LoginRequest("usuario1@email.com", "123456"))
                .as(LoginResponse.class)
                .getToken();
    }

    private void validarContrato(Response response, String schemaName) throws IOException {
        String fullPath = SCHEMAS_PATH + schemaName + ".json";
        JsonNode schemaNode;
        try (InputStream inputStream = Files.newInputStream(Paths.get(fullPath))) {
            schemaNode = mapper.readTree(inputStream);
        }

        String responseBody = response.getBody().asString();
        JsonNode responseNode = mapper.readTree(responseBody);
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        JsonSchema schema = schemaFactory.getSchema(schemaNode);
        Set<ValidationMessage> errors = schema.validate(responseNode);
        assertTrue("Falha na validação do contrato: " + errors, errors.isEmpty());
    }

    @Test
    public void testLoginSucessoEValidacaoDeContrato() throws IOException {
        LoginRequest loginBody = new LoginRequest("usuario1@email.com", "123456");

        Response response = apiService.fazerLogin(loginBody);

        response.then()
                .statusCode(200)
                .body("token", notNullValue());

        validarContrato(response, "login-sucesso-schema");
    }

    @Test
    public void testLoginComSenhaInvalida() {
        LoginRequest loginBody = new LoginRequest("usuario1@email.com", "senhaErrada");

        apiService.fazerLogin(loginBody)
                .then()
                .statusCode(500);
    }

    @Test
    public void testGetManutencoesComSucessoEValidacaoContrato() throws IOException {

        Response response = apiService.getManutencoes(userToken);

        response.then()
                .statusCode(200)
                .body("$", instanceOf(List.class));

        validarContrato(response, "manutencoes-sucesso-schema");
    }

    @Test
    public void testGetManutencoesSemToken() {
        Response response = apiService.getManutencoes("");

        response.then()
                .statusCode(403);
    }

    @Test
    public void testGetHistoricoConsumoComSucessoEValidacaoContrato() throws IOException {
        String idUnidadeExistente = "5";

        Response response = apiService.getHistoricoConsumo(userToken, idUnidadeExistente);

        response.then()
                .statusCode(200)
                .body("$", instanceOf(List.class));

        validarContrato(response, "historico-sucesso-schema");
    }

    @Test
    public void testGetHistoricoConsumoIdInexistente() {
        String idUnidadeInexistente = "9999";

        Response response = apiService.getHistoricoConsumo(userToken, idUnidadeInexistente);

        response.then()
                .statusCode(200)
                .body("$", empty());
    }

    @Test
    public void testPostOcorrenciaComAdminEValidacaoContrato() throws IOException {

        OcorrenciaRequest bodyOcorrencia = OcorrenciaRequest.builder()
                .idUnidade(6)
                .dataOcorrencia("2025-10-25")
                .tipoOcorrencia("Falha Teste JUnit")
                .descricao("Teste de API (JUnit)")
                .resolvida("N")
                .build();

        Response response = apiService.registrarOcorrencia(adminToken, bodyOcorrencia);

        response.then()
                .statusCode(201)
                .body("unidade.id", equalTo(6));

        validarContrato(response, "ocorrencia-sucesso-schema");
    }

    @Test
    public void testPostOcorrenciaComUserFalhaSeguranca() {

        OcorrenciaRequest bodyOcorrencia = OcorrenciaRequest.builder()
                .idUnidade(6)
                .dataOcorrencia("2025-10-25")
                .tipoOcorrencia("Teste de Invasao")
                .descricao("Usuario comum tentando postar")
                .resolvida("N")
                .build();

        apiService.registrarOcorrencia(userToken, bodyOcorrencia)
                .then()
                .statusCode(403);
    }

    @Test
    public void testPostOcorrenciaComAdminBodyInvalido() {

        OcorrenciaRequest bodyInvalido = OcorrenciaRequest.builder()
                .descricao("Tentativa com body invalido")
                .resolvida("S")
                .build();

        apiService.registrarOcorrencia(adminToken, bodyInvalido)
                .then()
                .statusCode(400);
    }

    @Test
    public void testPostMedicaoComAdminEValidacaoContrato() throws IOException {

        MedicaoRequest medicaoBody = MedicaoRequest.builder()
                .idUnidade(5)
                .dataLeitura("2025-10-24")
                .leituraAnterior(1700)
                .leituraAtual(1800)
                .observacao("Teste JUnit")
                .build();

        Response response = apiService.registrarMedicao(adminToken, medicaoBody);

        response.then()
                .statusCode(201)
                .body("consumoKwh", equalTo(100));

        validarContrato(response, "medicao-sucesso-schema");
    }

    @Test
    public void testPostMedicaoComUserFalhaSeguranca() {

        MedicaoRequest medicaoBody = MedicaoRequest.builder()
                .idUnidade(5)
                .dataLeitura("2025-10-24")
                .leituraAnterior(1900)
                .leituraAtual(2000)
                .observacao("Teste de Invasao")
                .build();

        apiService.registrarMedicao(userToken, medicaoBody)
                .then()
                .statusCode(403);
    }

    @Test
    public void testPostMedicaoComAdminBodyInvalido() {

        MedicaoRequest bodyInvalido = MedicaoRequest.builder()
                .idUnidade(5)
                .observacao("Tentativa com body invalido")
                .build();

        apiService.registrarMedicao(adminToken, bodyInvalido)
                .then()
                .statusCode(400);
    }

    @Test
    public void testGetAlertasConsumoComSucessoEValidacaoContrato() throws IOException {

        Response response = apiService.getAlertasConsumo(userToken);

        response.then()
                .statusCode(200)
                .body("$", instanceOf(List.class));

        validarContrato(response, "alertas-sucesso-schema");
    }

    @Test
    public void testGetAlertasConsumoSemToken() {

        Response response = apiService.getAlertasConsumo("");

        response.then()
                .statusCode(403);
    }
}



