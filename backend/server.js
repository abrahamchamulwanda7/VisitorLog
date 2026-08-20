const express = require('express');
const cors = require('cors');
const mysql = require('mysql2');

const app = express();
app.use(cors());
app.use(express.json());

const db = mysql.createConnection({
    host: '127.0.0.1',
    user: 'root',
    password: '2255',
    database: 'visitorlog_db'
});

db.connect((err) => {
    if (err) {
        console.log('Database connection failed: ' + err.message);
        return;
    }
    console.log('Connected to MariaDB database.');
});

app.get('/', (req, res) => {
    res.send('Visitor Log API is running.');
});

// Get all staff (for the "who are they visiting" dropdown)
app.get('/staff', (req, res) => {
    db.query('SELECT * FROM staff', (err, results) => {
        if (err) return res.status(500).json({ error: err.message });
        res.json(results);
    });
});

// Sign in a visitor
app.post('/visitors', (req, res) => {
    const { full_name, phone_number, purpose, staff_id, time_in } = req.body;
    const sql = 'INSERT INTO visitors (full_name, phone_number, purpose, staff_id, time_in) VALUES (?, ?, ?, ?, ?)';
    db.query(sql, [full_name, phone_number, purpose, staff_id, time_in], (err, result) => {
        if (err) return res.status(500).json({ error: err.message });
        res.json({ message: 'Visitor signed in', id: result.insertId });
    });
});

// Get all current visitors (not signed out yet)
app.get('/visitors/current', (req, res) => {
    const sql = `SELECT v.id, v.full_name, v.time_in, s.name AS staff_name
                 FROM visitors v JOIN staff s ON v.staff_id = s.id
                 WHERE v.time_out IS NULL`;
    db.query(sql, (err, results) => {
        if (err) return res.status(500).json({ error: err.message });
        res.json(results);
    });
});

// Sign out a visitor
app.put('/visitors/:id/signout', (req, res) => {
    const { time_out } = req.body;
    const sql = 'UPDATE visitors SET time_out = ? WHERE id = ? AND time_out IS NULL';
    db.query(sql, [time_out, req.params.id], (err, result) => {
        if (err) return res.status(500).json({ error: err.message });
        if (result.affectedRows === 0) {
            return res.status(404).json({ message: 'Visitor not found or already signed out' });
        }
        res.json({ message: 'Visitor signed out' });
    });
});

// Search visitors by name
app.get('/visitors/search', (req, res) => {
    const name = req.query.name || '';
    const sql = `SELECT v.id, v.full_name, v.time_in, v.time_out, s.name AS staff_name
                 FROM visitors v JOIN staff s ON v.staff_id = s.id
                 WHERE v.full_name LIKE ?`;
    db.query(sql, [`%${name}%`], (err, results) => {
        if (err) return res.status(500).json({ error: err.message });
        res.json(results);
    });
});

const PORT = 5000;
app.listen(PORT, () => {
    console.log(`Server running on http://localhost:${PORT}`);
});