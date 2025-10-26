package model;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MedicaoResponse {

    @Expose(serialize = false)
    private int id;

    @Expose(serialize = false)
    private Unidade unidade;

    @Expose(serialize = false)
    private String dataLeitura;

    @Expose(serialize = false)
    private int leituraAnterior;

    @Expose(serialize = false)
    private int leituraAtual;

    @Expose(serialize = false)
    private String observacao;

    @Expose(serialize = false)
    private int consumoKwh;
}