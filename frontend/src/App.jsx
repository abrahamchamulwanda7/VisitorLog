import { useState, useEffect } from 'react';
import './App.css';

const API_URL = 'http://localhost:5000';

function App() {
  const [staffList, setStaffList] = useState([]);
  const [currentVisitors, setCurrentVisitors] = useState([]);
  const [fullName, setFullName] = useState('');
  const [phone, setPhone] = useState('');
  const [purpose, setPurpose] = useState('');
  const [staffId, setStaffId] = useState('');
  const [timeIn, setTimeIn] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [hasSearched, setHasSearched] = useState(false);

  useEffect(() => {
    fetchStaff();
    fetchCurrentVisitors();
  }, []);

  const fetchStaff = async () => {
    const res = await fetch(`${API_URL}/staff`);
    const data = await res.json();
    setStaffList(data);
  };

  const fetchCurrentVisitors = async () => {
    const res = await fetch(`${API_URL}/visitors/current`);
    const data = await res.json();
    setCurrentVisitors(data);
  };

  const handleSignIn = async (e) => {
    e.preventDefault();
    await fetch(`${API_URL}/visitors`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        full_name: fullName,
        phone_number: phone,
        purpose: purpose,
        staff_id: staffId,
        time_in: timeIn,
      }),
    });
    setFullName('');
    setPhone('');
    setPurpose('');
    setStaffId('');
    setTimeIn('');
    fetchCurrentVisitors();
  };

  const handleSignOut = async (id) => {
    const now = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    await fetch(`${API_URL}/visitors/${id}/signout`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ time_out: now }),
    });
    fetchCurrentVisitors();
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    setHasSearched(true);
    const res = await fetch(`${API_URL}/visitors/search?name=${encodeURIComponent(searchTerm)}`);
    const data = await res.json();
    setSearchResults(data);
  };

  return (
    <div style={{ maxWidth: '600px', margin: '0 auto', padding: '20px', fontFamily: 'sans-serif' }}>
      <h1>Visitor Log</h1>

      <h2>Sign In a Visitor</h2>
      <form onSubmit={handleSignIn}>
        <div>
          <input
            type="text"
            placeholder="Full name"
            value={fullName}
            onChange={(e) => setFullName(e.target.value)}
            required
          />
        </div>
        <div>
          <input
            type="text"
            placeholder="Phone number"
            value={phone}
            onChange={(e) => setPhone(e.target.value)}
          />
        </div>
        <div>
          <input
            type="text"
            placeholder="Purpose of visit"
            value={purpose}
            onChange={(e) => setPurpose(e.target.value)}
          />
        </div>
        <div>
          <select value={staffId} onChange={(e) => setStaffId(e.target.value)} required>
            <option value="">Who are they visiting?</option>
            {staffList.map((s) => (
              <option key={s.id} value={s.id}>
                {s.name} ({s.department})
              </option>
            ))}
          </select>
        </div>
        <div>
          <input
            type="text"
            placeholder="Time in (e.g. 09:30)"
            value={timeIn}
            onChange={(e) => setTimeIn(e.target.value)}
            required
          />
        </div>
        <button type="submit">Sign In</button>
      </form>

      <h2>Currently In</h2>
      {currentVisitors.length === 0 ? (
        <p>No visitors currently in the building.</p>
      ) : (
        <ul>
          {currentVisitors.map((v) => (
            <li key={v.id}>
              {v.full_name} — Visiting: {v.staff_name} — In: {v.time_in}{' '}
              <button onClick={() => handleSignOut(v.id)}>Sign Out</button>
            </li>
          ))}
        </ul>
      )}

      <h2>Search Visitor History</h2>
      <form onSubmit={handleSearch}>
        <input
          type="text"
          placeholder="Search by name"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
        <button type="submit">Search</button>
      </form>
      {hasSearched && (
        searchResults.length === 0 ? (
          <p>No matching visitors found.</p>
        ) : (
          <ul>
            {searchResults.map((v) => (
              <li key={v.id}>
                {v.full_name} — Visiting: {v.staff_name} — In: {v.time_in} —{' '}
                {v.time_out ? `Left at ${v.time_out}` : 'Still in'}
              </li>
            ))}
          </ul>
        )
      )}
    </div>
  );
}

export default App;