import { useQuery } from "@tanstack/react-query";

const API_URL = "http://localhost:8080/politibiler?status=Tilgjengelig";

async function fetchAvailableCars() {
  const res = await fetch(API_URL);
  if (!res.ok) throw new Error(`Request failed: ${res.status}`);
  return res.json();
}

export default function App() {
  const {
    data: cars,
    isPending,
    error,
  } = useQuery({
    queryKey: ["cars", "Tilgjengelig"],
    queryFn: fetchAvailableCars,
  });

  if (isPending) return <p>Loading...</p>;
  if (error) return <p>Error: {error.message}</p>;

  return (
    <div style={{ padding: 16 }}>
      <h1>Available police cars</h1>
      {cars.length === 0 ? (
        <p>No cars available.</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Make</th>
              <th>Model</th>
              <th>Year</th>
              <th>Reg. no.</th>
              <th>Mission</th>
            </tr>
          </thead>
          <tbody>
            {cars.map(
              (car: {
                id: string;
                merke: string;
                modell: string;
                årsmodell: number;
                regNr: string;
                oppdrag: string;
              }) => (
                <tr key={car.id}>
                  <td>{car.id}</td>
                  <td>{car.merke}</td>
                  <td>{car.modell}</td>
                  <td>{car["årsmodell"]}</td>
                  <td>{car.regNr}</td>
                  <td>{car.oppdrag}</td>
                </tr>
              ),
            )}
          </tbody>
        </table>
      )}
    </div>
  );
}
