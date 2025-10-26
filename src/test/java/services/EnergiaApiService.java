package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.http.ContentType;
import io.restassured.path.json.mapper.factory.GsonObjectMapperFactory;
import io.restassured.response.Response;
import java.lang.reflect.Type;
import model.LoginRequest;
import model.MedicaoRequest;
import model.OcorrenciaRequest;
import static io.restassured.RestAssured.given;

public class EnergiaApiService {

    private static final String BASE_URI = "http://localhost:8080";

    static {
        RestAssured.baseURI = BASE_URI;

        GsonObjectMapperFactory factory = new GsonObjectMapperFactory() {
            @Override
            public Gson create(Type type, String s) {
                return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            }
        };

        RestAssured.config = RestAssured.config().objectMapperConfig(
                new ObjectMapperConfig().gsonObjectMapperFactory(factory)
        );
    }

    public EnergiaApiService() {

    }

    public Response fazerLogin(LoginRequest loginBody) {
        return given()
                .contentType(ContentType.JSON)
                .body(loginBody)
                .when()
                .post("/auth/login");
    }


    public Response getManutencoes(String token) {
        return given()
                .auth().oauth2(token)
                .when()
                .get("/manutencoes");
    }

    public Response registrarOcorrencia(String token, OcorrenciaRequest ocorrenciaBody) {
        return given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(ocorrenciaBody)
                .when()
                .post("/ocorrencia-tecnica");
    }

    public Response getHistoricoConsumo(String token, String idUnidade) {
        return given()
                .auth().oauth2(token)
                .when()
                .get("/historico-consumo/" + idUnidade);
    }

    public Response registrarMedicao(String token, MedicaoRequest medicaoBody) {
        return given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(medicaoBody)
                .when()
                .post("/medicao");
    }

    public Response getAlertasConsumo(String token) {
        return given()
                .auth().oauth2(token)
                .when()
                .get("/alertas-consumo");
    }
}