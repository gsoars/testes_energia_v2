package model;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HistoricoConsumoResponse {

    @Expose(serialize = false)
    private int idUnidade;

    @Expose(serialize = false)
    private String mesReferencia;

    @Expose(serialize = false)
    private int consumoKwh;

    @Expose(serialize = false)
    private double valorPago;
}