fun String.toLowercaseCamel(): String {
    val input = this.toLowerCase();
    val sb = StringBuilder();
    val delim = '_';
    var value: Char;
    var capitalize = false;
    for (i in 0..input.length) {
        value = input[i];
        if (value == delim) {
            capitalize = true;
        }
        else if (capitalize) {
            sb.append(value.toUpperCase());
            capitalize = false;
        }
        else {
            sb.append(value);
        }
    }
    return sb.toString();
}