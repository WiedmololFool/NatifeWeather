# NatifeWeather
В данном приложении используется три api для доступа к погодным данным:
- CurrentWeather и OneCall от OpenWeather (текущий и недельный прогноз);
- Places от Aeris (поиск городов);

Для работы OneCall API необходимо знать координаты широты и долготы пункта, а не название. Для их получения используется CurrentWeather API.
У OpenWeather нет API для поиска населенных пунктов, поэтому для этого используется дополнительное API от AERIS. 
Учитывая, что у Aeris доступно только 750 запросов в день (против 60 в минуту у OpenWeather), а покрытие обоих погодных сервисов в большинстве случаев совпадает, 
было принято решение не переводить полностью приложение на погодные API от Aeris.

!!! Поиск населенных пунктов с автозаполнением работает только на английском, т.к. Aeris не поддерживает запросы на другом языке !!!
