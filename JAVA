Чтобы добавить базу данных SQLite в ваш код, вам нужно будет использовать библиотеку для работы с SQLite в JavaScript. Одной из таких библиотек является `sql.js`, которая позволяет использовать SQLite в браузере. Однако, это значительно усложнит ваш код, так как вам нужно будет заменить все операции с `localStorage` на операции с базой данных.

Вот пример того, как можно интегрировать SQLite в ваш код с использованием `sql.js`:

1. Подключите `sql.js` к вашему проекту. Вы можете скачать библиотеку и добавить её в ваш проект, либо подключить её через CDN:

    ```html
    <script src="https://cdnjs.cloudflare.com/ajax/libs/sql.js/1.6.2/sql-wasm.js"></script>
    ```

2. Инициализируйте базу данных и создайте необходимые таблицы:

    ```javascript
    let db;

    initDatabase();

    async function initDatabase() {
        const SQL = await initSqlJs({
            locateFile: file => `https://cdnjs.cloudflare.com/ajax/libs/sql.js/1.6.2/${file}`
        });

        db = new SQL.Database();

        db.run(`
            CREATE TABLE IF NOT EXISTS game_data (
                key TEXT PRIMARY KEY,
                value TEXT
            );
        `);

        fillDatabase("tap-income", "1");
        fillDatabase("max-energy", "1000");
        fillDatabase("coins-for-up", "1000");
        fillDatabase("coin-count", "0");
        fillDatabase("hour-income", "3600");
        fillDatabase("current-energy", "1000");

        updateUI();
    }

    function fillDatabase(key, defaultValue) {
        const stmt = db.prepare("SELECT value FROM game_data WHERE key = ?");
        stmt.bind([key]);
        if (!stmt.step()) {
            const insertStmt = db.prepare("INSERT INTO game_data (key, value) VALUES (?, ?)");
            insertStmt.run([key, defaultValue]);
        }
        stmt.free();
    }

    function getDatabaseValue(key) {
        const stmt = db.prepare("SELECT value FROM game_data WHERE key = ?");
        stmt.bind([key]);
        let value = null;
        if (stmt.step()) {
            value = stmt.get()[0];
        }
        stmt.free();
        return value;
    }

    function setDatabaseValue(key, value) {
        const stmt = db.prepare("UPDATE game_data SET value = ? WHERE key = ?");
        stmt.run([value, key]);
        stmt.free();
    }

    function updateUI() {
        currentEnergyTxt.textContent = getDatabaseValue("current-energy");
        countTxt.textContent = getDatabaseValue("coin-count");
        tapIncomeTxt.textContent = getDatabaseValue("tap-income");
        coinsForUpTxt.textContent = getDatabaseValue("coins-for-up");
        hourIncomeTxt.textContent = getDatabaseValue("hour-income");
        maxEnergyTxt.textContent = getDatabaseValue("max-energy");
    }
    ```

3. Замените все операции с `localStorage` на операции с базой данных:

    ```javascript
    let maxEnergy = +getDatabaseValue("max-energy");

    setInterval(function refillEnergy() {
        let currentEnergy = +getDatabaseValue("current-energy");
        if (currentEnergy <= maxEnergy - 3) {
            currentEnergy += 3;
            setDatabaseValue("current-energy", currentEnergy);
            currentEnergyTxt.textContent = currentEnergy;
        } else {
            setDatabaseValue("current-energy", maxEnergy);
            currentEnergyTxt.textContent = maxEnergy;
        }
    }, 3000);

    button.addEventListener('click', function (event) {
        event.preventDefault();
        event.stopPropagation();

        let currentEnergy = +getDatabaseValue("current-energy");
        let tapIncome = +getDatabaseValue("tap-income");

        if (currentEnergy >= tapIncome) {
            currentEnergy -= tapIncome;
            setDatabaseValue("current-energy", currentEnergy);
            currentEnergyTxt.textContent = currentEnergy;

            let count = +getDatabaseValue("coin-count");
            count += tapIncome;
            setDatabaseValue("coin-count", count);
            countTxt.textContent = count;
        }
    });

    function upgrade() {
        let count = +getDatabaseValue("coin-count");
        if (count >= coinsForUpgrade) {
            count -= coinsForUpgrade;
            coinsForUpgrade += 10000;

            let passiveIncome = +getDatabaseValue("hour-income");
            passiveIncome += 3600;

            maxEnergy += 1000;

            let tapIncome = +getDatabaseValue("tap-income");
            tapIncome++;

            setDatabaseValue("coins-for-up", coinsForUpgrade);
            setDatabaseValue("hour-income", passiveIncome);
            setDatabaseValue("coin-count", count);
            setDatabaseValue("tap-income", tapIncome);
            setDatabaseValue("max-energy", maxEnergy);

            updateUI();

            console.log(`Upgrade successful! New hour income: ${passiveIncome}`);
        } else {
            console.log("Not enough coins for upgrade.");
        }
    }

    setInterval(function farmMoney() {
        let hourIncome = +getDatabaseValue("hour-income");
        let coins = +getDatabaseValue("coin-count");

        let secondIncome = Math.round(hourIncome / 3600);
        coins += secondIncome;

        console.log(`Passive income per second: ${secondIncome}`);
        console.log(`New coin count: ${coins}`);

        setDatabaseValue("coin-count", coins);
        countTxt.textContent = coins;
    }, 1000);

    upgradeButton.addEventListener("click", upgrade);
    ```

4. Обнуление базы данных:

    ```javascript
    function resetDatabase() {
        setDatabaseValue("tap-income", "1");
        setDatabaseValue("max-energy", "1000");
        setDatabaseValue("coins-for-up", "1000");
        setDatabaseValue("coin-count", "0");
        setDatabaseValue("hour-income", "3600");
        setDatabaseValue("current-energy", "1000");

        updateUI();

        console.log("Database has been reset to initial values.");
    }

    // resetDatabase();
    ```

Этот код заменяет использование `localStorage` на использование базы данных SQLite с помощью `sql.js`. Вы можете адаптировать этот пример под ваши нужды и добавить дополнительные функции при необходимости.
