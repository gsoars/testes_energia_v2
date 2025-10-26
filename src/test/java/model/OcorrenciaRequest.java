package model;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OcorrenciaRequest {

    @Expose
    private int idUnidade;

    @Expose
    private String dataOcorrencia;

    @Expose
    private String tipoOcorrencia;

    @Expose
    private String descricao;

    @Expose
    private String resolvida;
}