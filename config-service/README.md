# Configuration centralisee

`config-service` sert les configurations depuis `src/main/resources/configurations`.
Les proprietes communes sont dans `application.properties`; chaque application
possede aussi son fichier `<spring.application.name>.properties` ou `.yml`.

## Demarrage

Demarrer les applications dans cet ordre :

1. `config-service` sur le port `8888`
2. `discovery-service` (Eureka) sur le port `8761`
3. `customer-service` et `ebank-service` sur les ports `8082` et `8083`
4. `gateway-service` sur le port `9999` et `ebank-bot` sur le port `8066`

Les clients Config utilisent `CONFIG_SERVER_URL`, qui vaut par defaut
`http://localhost:8888`. Ils requierent le Config Server au demarrage : lancer
`config-service` en premier. Redemarrer une application cliente apres toute
modification de ses proprietes distantes.

## Verifications

- Sante du serveur : `http://localhost:8888/actuator/health`
- Configuration customer-service : `http://localhost:8888/customer-service/default`
- Configuration ebank-service : `http://localhost:8888/ebank-service/default`
- Configuration gateway-service : `http://localhost:8888/gateway-service/default`
- Configuration ebank-bot : `http://localhost:8888/ebank-bot/default`

Les valeurs sensibles ne sont pas stockees dans ce depot : fournir
`GROQ_API_KEY`, `DISCORD_TOKEN` et `TELEGRAM_TOKEN` dans l'environnement du
processus qui lance `ebank-bot`.
