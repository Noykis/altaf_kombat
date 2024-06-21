let button = document.querySelector(".button");
let countTxt = document.querySelector(".count__txt");

let tapIncomeTxt = document.querySelector(".tap-income");
let coinsForUpTxt = document.querySelector(".coins-for-up");
let hourIncomeTxt = document.querySelector(".hour-income");

let currentEnergyTxt = document.querySelector(".current-energy");
let maxEnergyTxt = document.querySelector(".max-energy");

let upgradeButton = document.querySelector(".upgrade__button");

let coinsForUpgrade = 1000;

function fillLocalStorage(key, defaultValue) {
    if (localStorage.getItem(key) === null) {
        localStorage.setItem(key, defaultValue);
    }
}

fillLocalStorage("tap-income", 1);
fillLocalStorage("max-energy", 1000);
fillLocalStorage("coins-for-up", 1000);
fillLocalStorage("coin-count", 0);
fillLocalStorage("hour-income", 3600);
fillLocalStorage("current-energy", localStorage.getItem("max-energy"));

let maxEnergy = +localStorage.getItem("max-energy");

currentEnergyTxt.textContent = localStorage.getItem("current-energy");
countTxt.textContent = localStorage.getItem("coin-count");
tapIncomeTxt.textContent = localStorage.getItem("tap-income");
coinsForUpTxt.textContent = localStorage.getItem("coins-for-up");
hourIncomeTxt.textContent = localStorage.getItem("hour-income");
maxEnergyTxt.textContent = localStorage.getItem("max-energy");

setInterval(function refillEnergy() {
    let currentEnergy = +localStorage.getItem("current-energy");
    if (currentEnergy <= maxEnergy - 3) {
        currentEnergy += 3;
        localStorage.setItem("current-energy", currentEnergy);
        currentEnergyTxt.textContent = currentEnergy;
    } else {
        localStorage.setItem("current-energy", maxEnergy);
        currentEnergyTxt.textContent = maxEnergy;
    }
}, 3000);

button.addEventListener('click', function (event) {
    event.preventDefault();
    event.stopPropagation();

    let currentEnergy = +localStorage.getItem("current-energy");
    let tapIncome = +localStorage.getItem("tap-income");

    if (currentEnergy >= tapIncome) {
        currentEnergy -= tapIncome;
        localStorage.setItem("current-energy", currentEnergy);
        currentEnergyTxt.textContent = currentEnergy;

        let count = +localStorage.getItem("coin-count");
        count += tapIncome;
        localStorage.setItem("coin-count", count);
        countTxt.textContent = count;
    }
});

function upgrade() {
    let count = +localStorage.getItem("coin-count");
    if (count >= coinsForUpgrade) {
        count -= coinsForUpgrade;
        coinsForUpgrade += 10000;

        let passiveIncome = +localStorage.getItem("hour-income");
        passiveIncome += 3600;

        maxEnergy += 1000;

        let tapIncome = +localStorage.getItem("tap-income");
        tapIncome++;

        localStorage.setItem("coins-for-up", coinsForUpgrade);
        localStorage.setItem("hour-income", passiveIncome);
        localStorage.setItem("coin-count", count);
        localStorage.setItem("tap-income", tapIncome);
        localStorage.setItem("max-energy", maxEnergy);

        countTxt.textContent = count;
        tapIncomeTxt.textContent = tapIncome;
        coinsForUpTxt.textContent = coinsForUpgrade;
        hourIncomeTxt.textContent = passiveIncome;
        maxEnergyTxt.textContent = maxEnergy;

        console.log(`Upgrade successful! New hour income: ${passiveIncome}`);
    } else {
        console.log("Not enough coins for upgrade.");
    }
}
 
setInterval(function farmMoney() {
    let hourIncome = +localStorage.getItem("hour-income");
    let coins = +localStorage.getItem("coin-count");

    let secondIncome = Math.round(hourIncome / 3600);
    coins += secondIncome;

    console.log(`Passive income per second: ${secondIncome}`);
    console.log(`New coin count: ${coins}`);

    localStorage.setItem("coin-count", coins);
    countTxt.textContent = coins;
}, 1000);

upgradeButton.addEventListener("click", upgrade);

//! ОБНУЛЕНИЕ
// function resetLocalStorage() {
//     localStorage.setItem("tap-income", 1);
//     localStorage.setItem("max-energy", 1000);
//     localStorage.setItem("coins-for-up", 1000);
//     localStorage.setItem("coin-count", 0);
//     localStorage.setItem("hour-income", 3600);
//     localStorage.setItem("current-energy", 1000);

//     // Обновление отображаемых значений на экране
//     currentEnergyTxt.textContent = localStorage.getItem("current-energy");
//     countTxt.textContent = localStorage.getItem("coin-count");
//     tapIncomeTxt.textContent = localStorage.getItem("tap-income");
//     coinsForUpTxt.textContent = localStorage.getItem("coins-for-up");
//     hourIncomeTxt.textContent = localStorage.getItem("hour-income");
//     maxEnergyTxt.textContent = localStorage.getItem("max-energy");

//     console.log("LocalStorage has been reset to initial values.");
// }

// resetLocalStorage();
