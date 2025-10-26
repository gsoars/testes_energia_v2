package model;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OcorrenciaResponse {

    @Expose(serialize = false)
    private int id;

    @Expose(serialize = false)
    private Unidade unidade;

    @Expose(serialize = false)
    private String dataOcorrencia;

    @Expose(serialize = false)
    private String tipoOcorrencia;

    @Expose(serialize = false)
    private String descricao;

    @Expose(serialize = false)
    private String resolvida;
}