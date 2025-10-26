package model;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Contrato {

    @Expose(serialize = false)
    private int id;

    @Expose(serialize = false)
    private Cliente cliente;

    @Expose(serialize = false)
    private String dataInicio;

    @Expose(serialize = false)
    private String dataFim;

    @Expose(serialize = false)
    private String statusContrato;

    @Expose(serialize = false)
    private String tipoTarifa;
}
