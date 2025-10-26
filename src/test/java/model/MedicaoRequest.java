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
public class MedicaoRequest {

    @Expose
    private int idUnidade;

    @Expose
    private String dataLeitura;

    @Expose
    private int leituraAnterior;

    @Expose
    private int leituraAtual;

    @Expose
    private String observacao;
}
