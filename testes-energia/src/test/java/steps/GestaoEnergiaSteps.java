package steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;
import model.LoginRequest;
import model.LoginResponse;
import model.MedicaoRequest;
import model.OcorrenciaRequest;
import services.EnergiaApiService;
import java.util.List;
import java.util.Map;

public class GestaoEnergiaSteps {

    private EnergiaApiService apiService;
    private Response response;
    private String token;

    @Quando("eu faco login com o email {string} e senha {string}")
    public void euFacoLoginComOEmailESenha(String email, String senha) {

        if (this.apiService == null) {
            this.apiService = new EnergiaApiService();
        }

        LoginRequest loginData = new LoginRequest(email, senha);

        this.response = apiService.fazerLogin(loginData);
    }

    @Entao("o status da resposta deve ser {int}")
    public void oStatusDaRespostaDeveSer(int status) {
        this.response.then().statusCode(status);
    }

    @E("a resposta deve conter um {string} de autenticacao")
    public void aRespostaDeveConterUmDeAutenticacao(String pathDoToken) {
        this.response.then().body(pathDoToken, notNullValue());

        LoginResponse loginResponse = this.response.as(LoginResponse.class);
        this.token = loginResponse.getToken();
    }

    @Dado("que eu estou autenticado como {string}")
    public void queEuEstouAutenticadoComo(String perfil) {
        if (this.apiService == null) {
            this.apiService = new EnergiaApiService();
        }

        LoginRequest loginBody;

        if ("ADMIN".equalsIgnoreCase(perfil)) {
            loginBody = new LoginRequest("usuario2@email.com", "123456");
        } else {
            loginBody = new LoginRequest("usuario1@email.com", "123456"); // E isso também
        }

        this.response = apiService.fazerLogin(loginBody);

        LoginResponse loginResponse = this.response.as(LoginResponse.class);
        this.token = loginResponse.getToken();
    }

    @Quando("eu envio uma nova medicao para a unidade {string} com os seguintes dados:")
    public void euEnvioUmaNovaMedicaoParaAUnidadeComOsSeguintesDados(String idUnidade, DataTable dataTable) {

        Map<String, String> data = dataTable.asMap();

        MedicaoRequest medicaoBody = MedicaoRequest.builder()
                .idUnidade(Integer.parseInt(idUnidade))
                .dataLeitura(data.get("dataLeitura"))
                .leituraAnterior(Integer.parseInt(data.get("leituraAnterior")))
                .leituraAtual(Integer.parseInt(data.get("leituraAtual")))
                .observacao(data.get("observacao").replace("\"", ""))
                .build();

        this.response = apiService.registrarMedicao(this.token, medicaoBody);
    }

    @E("a resposta deve conter o {string} calculado como {int}")
    public void aRespostaDeveConterOCalculadoComo(String pathDoConsumo, int calculoEsperado) {
        this.response.then().body(pathDoConsumo, equalTo(calculoEsperado));
    }

    @Quando("eu tento registrar uma ocorrencia tecnica para a unidade {string}")
    public void euTentoRegistrarUmaOcorrenciaTecnicaParaAUnidade(String idUnidade) {

        OcorrenciaRequest ocorrenciaBody = OcorrenciaRequest.builder()
                .idUnidade(Integer.parseInt(idUnidade))
                .dataOcorrencia("2025-10-24")
                .tipoOcorrencia("Falha Elétrica BDD")
                .descricao("Teste de segurança BDD")
                .resolvida("N")
                .build();

        this.response = apiService.registrarOcorrencia(this.token, ocorrenciaBody);
    }

    @Dado("que eu estou autenticado")
    public void queEuEstouAutenticado() {
        queEuEstouAutenticadoComo("UsuarioComum");
    }

    @Quando("eu consulto o historico de consumo da unidade {string}")
    public void euConsultoOHistoricoDeConsumoDaUnidade(String idUnidade) {
        this.response = apiService.getHistoricoConsumo(this.token, idUnidade);
    }

    @E("a resposta deve ser uma lista de consumos")
    public void aRespostaDeveSerUmaListaDeConsumos() {
        this.response.then().body("$", instanceOf(List.class));
    }

    @E("cada item da lista deve conter o {string} como {int}")
    public void cadaItemDaListaDeveConterOComo(String pathDoId, int idEsperado) {
        this.response.then().body(pathDoId, everyItem(equalTo(idEsperado)));
    }
}