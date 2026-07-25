import { useState } from "react";
import "./App.css";

function App() {
  const [nombre, setNombre] = useState("");
  const [documento, setDocumento] = useState("");
  const [valor, setValor] = useState("");

  const guardarPrestamo = async (e) => {
    e.preventDefault();

    const prestamo = {
      nombre: nombre,
      monto: Number(valor),
      plazoMeses: 12,
      tasaInteres: 5,
      estado: "Pendiente",
    };

    try {
      const respuesta = await fetch("http://localhost:8081/prestamos", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(prestamo),
      });

      if (respuesta.ok) {
        alert("Préstamo guardado correctamente");
        setNombre("");
        setDocumento("");
        setValor("");
      } else {
        alert("Error al guardar el préstamo");
      }
    } catch (error) {
      console.error(error);
      alert("No se pudo conectar con el servidor");
    }
  };

  return (
    <div className="contenedor">
      <h1>Sistema de Préstamos</h1>

      <form onSubmit={guardarPrestamo}>
        <label>Nombre:</label>
        <br />
        <input
          type="text"
          value={nombre}
          onChange={(e) => setNombre(e.target.value)}
        />

        <br />
        <br />

        <label>Documento:</label>
        <br />
        <input
          type="text"
          value={documento}
          onChange={(e) => setDocumento(e.target.value)}
        />

        <br />
        <br />

        <label>Valor del préstamo:</label>
        <br />
        <input
          type="number"
          value={valor}
          onChange={(e) => setValor(e.target.value)}
        />

        <br />
        <br />

        <button type="submit">Guardar préstamo</button>
      </form>
    </div>
  );
}

export default App;