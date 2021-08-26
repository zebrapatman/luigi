document.getElementsByName("van")[0].oninvalid = function() {
    this.setCustomValidity("Verplicht"); };