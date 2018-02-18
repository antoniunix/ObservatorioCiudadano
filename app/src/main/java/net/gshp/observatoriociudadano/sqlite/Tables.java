package net.gshp.observatoriociudadano.sqlite;

/**
 * Created by Antoniunix on 3/10/17.
 */

public class Tables {

    public final String TABLE_SCHEDULE = "CREATE TABLE agenda("
            + "id INTEGER NOT NULL,"
            + "id_user INTEGER,"
            + "id_place INTEGER,"
            + "start_datetime TEXT,"
            + "end_datetime TEXT," +
            "lastUpdate INTEGER," +
            "id_rol INTEGER)";


    public final String TABLE_PDV = "CREATE TABLE pdv("
            + "id INTEGER NOT NULL,"
            + "id_client INTEGER NOT NULL,"
            + "id_rtm INTEGER NOT NULL,"
            + "name TEXT,"
            + "address TEXT,"
            + "town INTEGER,"
            + "id_format INTEGER,"
            + "pdv_code TEXT,"
            + "lat REAL,"
            + "lon REAL,"
            + "extra_field1 TEXT,"
            + "extra_field2 TEXT,"
            + "extra_field3 TEXT,"
            + "type INTEGER," +
            " id_region INTEGER," +
            "id_rol INTEGER)";


    public final String TableReportGeo = "CREATE TABLE report_geolocation(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "latitude TEXT," +
            "longitude TEXT," +
            "battery TEXT," +
            "accuracy TEXT," +
            "imei TEXT," +
            "satellite_utc TEXT," +
            "date TEXT," +
            "tz TEXT," +
            "version TEXT," +
            "send INTEGER," +
            "hash TEXT," +
            "provider TEXT," +
            "fakelocation_enabled INTEGER)";

    public final String TablePolitics = "CREATE TABLE politics("
            + "version TEXT,"
            + "politic TEXT)";


    public final String TableReportReport = "CREATE TABLE report(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "id_pdv INTEGER," +
            "id_agenda INTEGER," +
            "version TEXT," +
            "date TEXT," +
            "tz TEXT," +
            "imei TEXT," +
            "hash TEXT," +
            "send INTEGER," +
            "type_report INTEGER," +
            "id_report_server INTEGER," +
            "date_inactive TEXT," +
            "active INTEGER," +
            "type_poll INTEGER)";// es para saber si es reporte de alta supervisor o representante

    public final String TableReportCheck = "CREATE TABLE report_check(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "id_report_local INTEGER," +
            "date TEXT," +
            "tz TEXT," +
            "latitude TEXT," +
            "longitude TEXT," +
            "accuracy TEXT," +
            "imei TEXT," +
            "satellite_utc TEXT," +
            "type INTEGER, " +
            "send INTEGER," +
            "provider TEXT," +
            "date_inactive TEXT," +
            "active INTEGER," +
            "hash TEXT)";


    /**
     * Encuesta
     */

    public final String Table_EAEncuesta = "CREATE TABLE EAEncuesta(" + "id INTEGER PRIMARY KEY NOT NULL,"
            + "nombre TEXT," + "vigenciaInicial INTEGER," + "vigenciaFinal INTEGER," + "repeticiones INTEGER,"
            + "canal TEXT," + "rtm TEXT," + "cliente TEXT," + "pdv TEXT," + "query TEXT," + "descripcion TEXT,"
            + "rtEnabled INTEGER,estado Integer,region TEXT,restriction INTEGER)";

    public final String Table_EAGrupo = "CREATE TABLE EAGrupo(" + "id INTEGER NOT NULL," + "nombre TEXT)";

    public final String Table_EAOpcionPregunta = "CREATE TABLE EAOpcionPregunta(" + "idPregunta INTEGER,"
            + "opcion TEXT," + "image TEXT)";

    public final String Table_EAPregunta = "CREATE TABLE EAPregunta(" + "id INTEGER PRIMARY KEY NOT NULL,"
            + "idSeccion INTEGER," + "idEncuesta INTEGER," + "idGrupo INTEGER," + "pregunta TEXT," + "parentId INTEGER,"
            + "idTipoPregunta INTEGER," + "obligatoria INTEGER," + "RangoMinimo TEXT," + "RangoMaximo TEXT,"
            + "orden INTEGER," + "peso INTEGER," + "operadorDependencia TEXT," + "valorDependencia1 TEXT,"
            + "valorDependencia2 TEXT," + "queryOpcionesDependencia TEXT," + "queryVisibility INTEGER,"
            + "queryOpciones TEXT)";

    public final String Table_EARespuesta = "CREATE TABLE EARespuesta("
            + "ida INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + "idPregunta INTEGER, " + "idReporte INTEGER,"
            + "idReporteLocal INTEGER," + "idEncuesta INTEGER,"
            + "nombreEncuesta TEXT," + "respuesta TEXT," + "hash TEXT," + "enviado INTEGER," + "numeroEncuesta INTEGER,"
            + "campoExtra1 TEXT," + "campoExtra2 TEXT," + "timeStamp TEXT)";

    public final String Table_EARespuestaRT = "CREATE TABLE EARespuestaRT("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + "idPregunta INTEGER, " + "idEncuesta INTEGER, "
            + "nombreEncuesta TEXT," + "respuesta TEXT," + "numeroEncuesta INTEGER," + "campoExtra1 TEXT,"
            + "campoExtra2 TEXT," + "timeStamp TEXT," + "idPdv INTEGER," + "hash TEXT," + "enviado INTEGER" + ")";

    public final String Table_EASeccion = "CREATE TABLE EASeccion(" + "id INTEGER PRIMARY KEY NOT NULL,"
            + "idEncuesta INTEGER," + "idParent INTEGER," + "orden INTEGER," + "peso INTEGER," + "nombre TEXT)";

    public final String TableEAAnswerPdv = "CREATE TABLE EAAnswerPdv(" +
            "id_pdv INTEGER," +
            "id_poll INTEGER)";

    public final String Table_EATipoPregunta = "CREATE TABLE EATipoPregunta(" + "id INTEGER PRIMARY KEY NOT NULL,"
            + "tipo TEXT)";

    public final String TablePhoto = "CREATE TABLE photo(" +
            "id INTEGER PRIMARY KEY," +
            "path TEXT," +
            "name TEXT," +
            "face_id INTEGER," +
            "sent INTEGER," +
            "rol INTEGER)";
    public final String TableImageLogin = "CREATE TABLE image_login(" +
            "id INTEGER PRIMARY KEY," +
            "path TEXT," +
            "name TEXT," +
            "sent INTEGER," +
            "rol INTEGER)";

    public final String TableSepomex = "CREATE TABLE sepomex(" +
            "postal_code TEXT," +
            "suburb INTEGER," +
            "type_suburb INTEGER," +
            "town TEXT," +
            "state TEXT)";

    public final String TableCensus = "CREATE TABLE report_census(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "id_report_local INTEGER," +
            "lat TEXT," +
            "lon TEXT," +
            "suburb TEXT," +
            "town TEXT," +
            "state TEXT," +
            "cp real INTEGER," +
            "external_number TEXT," +
            "address TEXT," +
            "name_street TEXT," +
            "hash TEXT," +
            "provider TEXT," +
            "send INTEGER)";


}
