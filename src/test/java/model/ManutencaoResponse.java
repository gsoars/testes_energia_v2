package model;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ManutencaoResponse {

    @Expose(serialize = false)
    private int id;

    @Expose(serialize = false)
    private Equipamento equipamento;

    @Expose(serialize = false)
    private String dataManutencao;

    @Expose(serialize = false)
    private String descricao;

    @Expose(serialize = false)
    private String tipoManutencao;

    @Expose(serialize = false)
    private String responsavel;
}
