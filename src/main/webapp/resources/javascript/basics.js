function openNav() {
    document.getElementById("sideNav").style.width = "250px";
    $("#sideNav").show(500, "linear");
}

function closeNav() {
    document.getElementById("sideNav").style.width = "0";
    $("#sideNav").hide(500, "linear");
}

function redirect(url) {
    var pageUrl = $(location).attr("href");
    url = pageUrl.toString() + url.toString();
    window.location.href = url;
}
//scr: https://www.primefaces.org/showcase/ui/input/datepicker/datePickerJava8.xhtml?jfwid=28da3
PrimeFaces.locales ['de'] = {
    closeText: 'Schließen',
    prevText: 'Zurück',
    nextText: 'Weiter',
    monthNames: ['Januar', 'Februar', 'März', 'April', 'Mai', 'Juni', 'Juli', 'August', 'September', 'Oktober', 'November', 'Dezember'],
    monthNamesShort: ['Jan', 'Feb', 'Mär', 'Apr', 'Mai', 'Jun', 'Jul', 'Aug', 'Sep', 'Okt', 'Nov', 'Dez'],
    dayNames: ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag'],
    dayNamesShort: ['Son', 'Mon', 'Die', 'Mit', 'Don', 'Fre', 'Sam'],
    dayNamesMin: ['S', 'M', 'D', 'M ', 'D', 'F ', 'S'],
    weekHeader: 'Woche',
    firstDay: 1,
    isRTL: false,
    showMonthAfterYear: false,
    yearSuffix: '',
    timeOnlyTitle: 'Nur Zeit',
    timeText: 'Zeit',
    hourText: 'Stunde',
    minuteText: 'Minute',
    secondText: 'Sekunde',
    millisecondText: 'Millisekunde',
    currentText: 'Aktuelles Datum',
    ampm: false,
    month: 'Monat',
    week: 'Woche',
    day: 'Tag',
    allDayText: 'Ganzer Tag',
    today: 'Heute',
    clear: 'Löschen'
};