
var express = require('express');
var router = express.Router();

//Testdaten für Salesmen und Performance-Datensätze
var salesmen = [
    {
        firstname: 'Max',
        lastname: 'Mustermann',
        sid: 1,
        performanceRecords: [
            {
                goalDescription: 'Umsatzziel',
                targetValue: 100,
                actualValue: 120,
                year: 2025
            },
            {
                goalDescription: 'Kundenzufriedenheit',
                targetValue: 90,
                actualValue: 85,
                year: 2025
            },
            {
                goalDescription: 'Umsatzziel',
                targetValue: 150,
                actualValue: 140,
                year: 2026
            }
        ]
    },
    {
        firstname: 'Anna',
        lastname: 'Schmidt',
        sid: 2,
        performanceRecords: [
            {
                goalDescription: 'Neukundengewinnung',
                targetValue: 20,
                actualValue: 25,
                year: 2025
            },
            {
                goalDescription: 'Kundenzufriedenheit',
                targetValue: 95,
                actualValue: 92,
                year: 2026
            }
        ]
    },
    {
        firstname: 'Peter',
        lastname: 'Müller',
        sid: 3,
        performanceRecords: [
            {
                goalDescription: 'Umsatzziel',
                targetValue: 200,
                actualValue: 180,
                year: 2025
            },
            {
                goalDescription: 'Neukundengewinnung',
                targetValue: 30,
                actualValue: 35,
                year: 2026
            },
            {
                goalDescription: 'Weiterbildung',
                targetValue: 2,
                actualValue: 2,
                year: 2026
            }
        ]
    },
    {
        firstname: 'Laura',
        lastname: 'Weber',
        sid: 4,
        performanceRecords: [
            {
                goalDescription: 'Kundenzufriedenheit',
                targetValue: 90,
                actualValue: 96,
                year: 2025
            },
            {
                goalDescription: 'Umsatzziel',
                targetValue: 120,
                actualValue: 130,
                year: 2026
            }
        ]
    }
];


// GET: Alle Salesman abrufen
router.get('/', function(req, res) {
    res.json(salesmen);
});

// GET: Einen Salesman anhand seiner ID abrufen
router.get('/:sid', function(req, res) {
    const sid = parseInt(req.params.sid);

    const salesman = salesmen.find(function(salesman) {
        return salesman.sid === sid;
    });

    if (!salesman) {
        return res.status(404).json({
            error: 'Salesman not found'
        });
    }

    res.json(salesman);
});

// GET: Performance-Datensätze eines Salesman abrufen
router.get('/:sid/performance-records', function(req, res) {
    const sid = parseInt(req.params.sid);

    const salesman = salesmen.find(function(salesman) {
        return salesman.sid === sid;
    });

    if (!salesman) {
        return res.status(404).json({
            error: 'Salesman not found'
        });
    }

    res.json(salesman.performanceRecords);
});

// GET: Performance-Datensätze eines Salesman nach Jahr abrufen
router.get('/:sid/performance-records/:year', function(req, res) {
    const sid = parseInt(req.params.sid);
    const year = parseInt(req.params.year);

    const salesman = salesmen.find(function(salesman) {
        return salesman.sid === sid;
    });

    if (!salesman) {
        return res.status(404).json({
            error: 'Salesman not found'
        });
    }

    const records = salesman.performanceRecords.filter(function(record) {
        return record.year === year;
    });

    res.json(records);
});

// POST: Neuen Salesman erstellen
router.post('/', function(req, res) {
    const salesman = req.body;

    salesman.performanceRecords = [];

    salesmen.push(salesman);

    res.status(201).json(salesman);
});

// POST: Performance-Datensatz zu einem Salesman hinzufügen
router.post('/:sid/performance-records', function(req, res) {
    const sid = parseInt(req.params.sid);

    const salesman = salesmen.find(function(salesman) {
        return salesman.sid === sid;
    });

    if (!salesman) {
        return res.status(404).json({
            error: 'Salesman not found'
        });
    }

    const performanceRecord = req.body;

    salesman.performanceRecords.push(performanceRecord);

    res.status(201).json(performanceRecord);
});

// DELETE: Salesman anhand seiner ID löschen
router.delete('/:sid', function(req, res) {
    const sid = parseInt(req.params.sid);

    const index = salesmen.findIndex(function(salesman) {
        return salesman.sid === sid;
    });

    if (index === -1) {
        return res.status(404).json({
            error: 'Salesman not found'
        });
    }

    salesmen.splice(index, 1);

    res.status(204).send();
});

// DELETE: Alle Performance-Datensätze eines Salesman löschen
router.delete('/:sid/performance-records', function(req, res) {
    const sid = parseInt(req.params.sid);

    const salesman = salesmen.find(function(salesman) {
        return salesman.sid === sid;
    });

    if (!salesman) {
        return res.status(404).json({
            error: 'Salesman not found'
        });
    }

    salesman.performanceRecords = [];

    res.status(204).send();
});

module.exports = router;
