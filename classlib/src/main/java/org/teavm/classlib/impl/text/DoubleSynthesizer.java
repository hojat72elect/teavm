/*
 *  Copyright 2023 Alexey Andreev.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.teavm.classlib.impl.text;

public final class DoubleSynthesizer {
    private DoubleSynthesizer() {
    }

    public static double synthesizeDouble(long mantissa, int exp, boolean negative) {
        var indexInTable = DoubleAnalyzer.MAX_ABS_DEC_EXP - exp;
        if (mantissa == 0 || indexInTable > DoubleSynthesizer.mantissa10Table.length || indexInTable < 0) {
            return Double.longBitsToDouble(negative ? (1L << 63) : 0);
        }

        var binMantissa = DoubleAnalyzer.mulAndShiftRight(mantissa, DoubleSynthesizer.mantissa10Table[indexInTable], 0);
        var binExp = DoubleSynthesizer.exp10Table[indexInTable] - 1;
        while ((binMantissa & (-1L << 58L)) != 0) {
            binMantissa >>>= 1;
            binExp++;
        }
        while (binMantissa < (1L << 57)) {
            binMantissa <<= 1;
            binExp--;
        }
        if (binExp >= 2047) {
            return negative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        }
        binMantissa += 1L << 4;
        if (binExp <= 0) {
            binMantissa >>= Math.min(-binExp + 1, 64);
            binExp = 0;
        }

        binMantissa = (binMantissa >>> 5) & (-1L << 12 >>> 12);
        var iee754 = binMantissa | ((long) binExp << 52);
        if (negative) {
            iee754 ^= 1L << 63;
        }
        return Double.longBitsToDouble(iee754);
    }

    // Numbers in the table below are generated by DoubleSynthesizerGenerator

    private static final long[] mantissa10Table = {
            -5920691823653471754L,
            -8425902273664687727L,
            -2413397193637769393L,
            -5620066569652125837L,
            -8185402070463610993L,
            -2028596868516046619L,
            -5312226309554747619L,
            -7939129862385708418L,
            -1634561335591402499L,
            -4996997883215032323L,
            -7686947121313936181L,
            -1231068949876566920L,
            -4674203974643163860L,
            -7428711994456441411L,
            -817892746904575288L,
            -4343663012265570553L,
            -7164279224554366766L,
            -394800315061255856L,
            -4005189066790915008L,
            -6893500068174642330L,
            -9204148869281624187L,
            -3658591746624867729L,
            -6616222212041804507L,
            -8982326584375353929L,
            -3303676090774835316L,
            -6332289687361778576L,
            -8755180564631333184L,
            -2940242459184402125L,
            -6041542782089432023L,
            -8522583040413455942L,
            -2568086420435798537L,
            -5743817951090549153L,
            -8284403175614349646L,
            -2186998636757228463L,
            -5438947724147693094L,
            -8040506994060064798L,
            -1796764746270372707L,
            -5126760611758208489L,
            -7790757304148477115L,
            -1397165242411832414L,
            -4807081008671376254L,
            -7535013621679011327L,
            -987975350460687153L,
            -4479729095110460046L,
            -7273132090830278360L,
            -568964901102714406L,
            -4144520735624081848L,
            -7004965403241175802L,
            -139898200960150313L,
            -3801267375510030573L,
            -6730362715149934782L,
            -9073638986861858149L,
            -3449775934753242068L,
            -6449169562544503978L,
            -8848684464777513506L,
            -3089848699418290639L,
            -6161227774276542835L,
            -8618331034163144591L,
            -2721283210435300376L,
            -5866375383090150624L,
            -8382449121214030822L,
            -2343872149716718346L,
            -5564446534515285000L,
            -8140906042354138323L,
            -1957403223540890347L,
            -5255271393574622601L,
            -7893565929601608404L,
            -1561659043136842477L,
            -4938676049251384305L,
            -7640289654143017767L,
            -1156417002403097458L,
            -4614482416664388289L,
            -7380934748073420955L,
            -741449152691742558L,
            -4282508136895304370L,
            -7115355324258153819L,
            -316522074587315140L,
            -3942566474411762436L,
            -6843401994271320272L,
            -9164070410158966541L,
            -3594466212028615495L,
            -6564921784364802720L,
            -8941286242233752499L,
            -3238011543348273028L,
            -6279758049420528746L,
            -8713155254278333320L,
            -2873001962619602342L,
            -5987750384837592197L,
            -8479549122611984081L,
            -2499232151953443560L,
            -5688734536304665171L,
            -8240336443785642460L,
            -2116491865831296966L,
            -5382542307406947896L,
            -7995382660667468640L,
            -1724565812842218855L,
            -5069001465015685407L,
            -7744549986754458649L,
            -1323233534581402868L,
            -4747935642407032618L,
            -7487697328667536418L,
            -912269281642327298L,
            -4419164240055772162L,
            -7224680206786528053L,
            -491441886632713915L,
            -4082502324048081455L,
            -6955350673980375487L,
            -60514634142869810L,
            -3737760522056206171L,
            -6679557232386875260L,
            -9032994600651410532L,
            -3384744916816525881L,
            -6397144748195131028L,
            -8807064613298015146L,
            -3023256937051093263L,
            -6107954364382784934L,
            -8575712306248138270L,
            -2653093245771290262L,
            -5811823411358942533L,
            -8338807543829064350L,
            -2274045625900771990L,
            -5508585315462527915L,
            -8096217067111932656L,
            -1885900863153361279L,
            -5198069505264599346L,
            -7847804418953589800L,
            -1488440626100012711L,
            -4880101315621920492L,
            -7593429867239446717L,
            -1081441343357383777L,
            -4554501889427817345L,
            -7332950326284164199L,
            -664674077828931749L,
            -4221088077005055722L,
            -7066219276345954901L,
            -237904397927796872L,
            -3879672333084147821L,
            -6793086681209228580L,
            -9123818159709293187L,
            -3530062611309138130L,
            -6513398903789220827L,
            -8900067937773286985L,
            -3172062256211528206L,
            -6226998619711132888L,
            -8670947710510816634L,
            -2805469892591575644L,
            -5933724728815170839L,
            -8436328597794046994L,
            -2430079312244744221L,
            -5633412264537705700L,
            -8196078626372074883L,
            -2045679357969588844L,
            -5325892301117581398L,
            -7950062655635975442L,
            -1652053804791829737L,
            -5010991858575374113L,
            -7698142301602209614L,
            -1248981238337804412L,
            -4688533805412153853L,
            -7440175859071633406L,
            -836234930288882479L,
            -4358336758973016307L,
            -7176018221920323369L,
            -413582710846786420L,
            -4020214983419339459L,
            -6905520801477381891L,
            -9213765455923815836L,
            -3673978285252374367L,
            -6628531442943809817L,
            -8992173969096958177L,
            -3319431906329402113L,
            -6344894339805432014L,
            -8765264286586255934L,
            -2956376414312278525L,
            -6054449946191733143L,
            -8532908771695296838L,
            -2584607590486743971L,
            -5757034887131305500L,
            -8294976724446954723L,
            -2203916314889396588L,
            -5452481866653427593L,
            -8051334308064652398L,
            -1814088448677712867L,
            -5140619573684080617L,
            -7801844473689174817L,
            -1414904713676948737L,
            -4821272585683469313L,
            -7546366883288685774L,
            -1006140569036166268L,
            -4494261269970843337L,
            -7284757830718584993L,
            -587566084924005019L,
            -4159401682681114339L,
            -7016870160886801794L,
            -158945813193151901L,
            -3816505465296431844L,
            -6742553186979055799L,
            -9083391364325154962L,
            -3465379738694516970L,
            -6461652605697523899L,
            -8858670899299929442L,
            -3105826994654156138L,
            -6174010410465235234L,
            -8628557143114098510L,
            -2737644984756826647L,
            -5879464802547371641L,
            -8392920656779807636L,
            -2360626606621961247L,
            -5577850100039479321L,
            -8151628894773493780L,
            -1974559787411859078L,
            -5268996644671397586L,
            -7904546130479028392L,
            -1579227364540714458L,
            -4952730706374481889L,
            -7651533379841495835L,
            -1174406963520662366L,
            -4628874385558440216L,
            -7392448323188662496L,
            -759870872876129024L,
            -4297245513042813542L,
            -7127145225176161157L,
            -335385916056126881L,
            -3957657547586811828L,
            -6855474852811359786L,
            -9173728696990998152L,
            -3609919470959866074L,
            -6577284391509803182L,
            -8951176327949752869L,
            -3253835680493873621L,
            -6292417359137009220L,
            -8723282702051517699L,
            -2889205879056697349L,
            -6000713517987268202L,
            -8489919629131724885L,
            -2515824962385028846L,
            -5702008784649933400L,
            -8250955842461857044L,
            -2133482903713240300L,
            -5396135137712502563L,
            -8006256924911912374L,
            -1741964635633328828L,
            -5082920523248573386L,
            -7755685233340769032L,
            -1341049929119499481L,
            -4762188758037509908L,
            -7499099821171918250L,
            -930513269649338230L,
            -4433759430461380907L,
            -7236356359111015049L,
            -510123730351893109L,
            -4097447799023424810L,
            -6967307053960650171L,
            -79644842111309304L,
            -3753064688430957767L,
            -6691800565486676537L,
            -9042789267131251553L,
            -3400416383184271515L,
            -6409681921289327535L,
            -8817094351773372351L,
            -3039304518611664792L,
            -6120792429631242157L,
            -8585982758446904049L,
            -2669525969289315508L,
            -5824969590173362730L,
            -8349324486880600507L,
            -2290872734783229842L,
            -5522047002568494197L,
            -8106986416796705681L,
            -1903131822648998119L,
            -5211854272861108819L,
            -7858832233030797378L,
            -1506085128623544835L,
            -4894216917640746191L,
            -7604722348854507276L,
            -1099509313941480672L,
            -4568956265895094861L,
            -7344513827457986212L,
            -683175679707046970L,
            -4235889358507547899L,
            -7078060301547948643L,
            -256850038250986858L,
            -3894828845342699810L,
            -6805211891016070171L,
            -9133518327554766460L,
            -3545582879861895366L,
            -6525815118631426616L,
            -8910000909647051616L,
            -3187955011209551616L,
            -6239712823709551616L,
            -8681119073709551616L,
            -2821744073709551616L,
            -5946744073709551616L,
            -8446744073709551616L,
            -2446744073709551616L,
            -5646744073709551616L,
            -8206744073709551616L,
            -2062744073709551616L,
            -5339544073709551616L,
            -7960984073709551616L,
            -1669528073709551616L,
            -5024971273709551616L,
            -7709325833709551616L,
            -1266874889709551616L,
            -4702848726509551616L,
            -7451627795949551616L,
            -854558029293551616L,
            -4372995238176751616L,
            -7187745005283311616L,
            -432345564227567616L,
            -4035225266123964416L,
            -6917529027641081856L,
            -9223372036854775808L,
            -3689348814741910324L,
            -6640827866535438582L,
            -9002011107970261189L,
            -3335171328526686933L,
            -6357485877563259869L,
            -8775337516792518219L,
            -2972493582642298180L,
            -6067343680855748868L,
            -8543223759426509417L,
            -2601111570856684098L,
            -5770238071427257602L,
            -8305539271883716405L,
            -2220816390788215277L,
            -5466001927372482545L,
            -8062150356639896359L,
            -1831394126398103205L,
            -5154464115860392887L,
            -7812920107430224633L,
            -1432625727662628443L,
            -4835449396872013078L,
            -7557708332239520786L,
            -1024286887357502287L,
            -4508778324627912153L,
            -7296371474444240046L,
            -606147914885053103L,
            -4174267146649952806L,
            -7028762532061872568L,
            -177973607073265139L,
            -3831727700400522434L,
            -6754730975062328271L,
            -9093133594791772940L,
            -3480967307441105734L,
            -6474122660694794911L,
            -8868646943297746252L,
            -3121788665050663033L,
            -6186779746782440750L,
            -8638772612167862923L,
            -2753989735242849707L,
            -5892540602936190089L,
            -8403381297090862394L,
            -2377363631119648861L,
            -5591239719637629412L,
            -8162340590452013853L,
            -1991698500497491195L,
            -5282707615139903279L,
            -7915514906853832947L,
            -1596777406740401745L,
            -4966770740134231719L,
            -7662765406849295699L,
            -1192378206733142148L,
            -4643251380128424042L,
            -7403949918844649557L,
            -778273425925708321L,
            -4311967555482476980L,
            -7138922859127891907L,
            -354230130378896082L,
            -3972732919045027189L,
            -6867535149977932074L,
            -9183376934724255983L,
            -3625356651333078602L,
            -6589634135808373205L,
            -8961056123388608887L,
            -3269643353196043250L,
            -6305063497298744923L,
            -8733399612580906262L,
            -2905392935903719049L,
            -6013663163464885563L,
            -8500279345513818773L,
            -2532400508596379068L,
            -5715269221619013577L,
            -8261564192037121185L,
            -2150456263033662926L,
            -5409713825168840664L,
            -8017119874876982855L,
            -1759345355577441598L,
            -5096825099203863602L,
            -7766808894105001205L,
            -1358847786342270957L,
            -4776427043815727089L,
            -7510490449794491995L,
            -948738275445456222L,
            -4448339435098275301L,
            -7248020362820530564L,
            -528786136287117932L,
            -4112377723771604669L,
            -6979250993759194058L,
            -98755145788979524L,
            -3768352931373093942L,
            -6704031159840385477L,
            -9052573742614218705L,
            -3416071543957018958L,
            -6422206049907525490L,
            -8827113654667930715L,
            -3055335403242958174L,
            -6133617137336276863L,
            -8596242524610931813L,
            -2685941595151759932L,
            -5838102090863318269L,
            -8359830487432564938L,
            -2307682335666372931L,
            -5535494683275008668L,
            -8117744561361917258L,
            -1920344853953336643L,
            -5225624697904579637L,
            -7869848573065574033L,
            -1523711272679187483L,
            -4908317832885260310L,
            -7616003081050118571L,
            -1117558485454458744L,
            -4583395603105477319L,
            -7356065297226292178L,
            -701658031336336515L,
            -4250675239810979535L,
            -7089889006590693952L,
            -275775966319379353L,
            -3909969587797413806L,
            -6817324484979841368L,
            -9143208402725783417L,
            -3561087000135522498L,
            -6538218414850328322L,
            -8919923546622172981L,
            -3203831230369745799L,
            -6252413799037706963L,
            -8691279853972075893L,
            -2838001322129590460L,
            -5959749872445582691L,
            -8457148712698376476L,
            -2463391496091671392L,
            -5660062011615247437L,
            -8217398424034108273L,
            -2079791034228842266L,
            -5353181642124984136L,
            -7971894128441897632L,
            -1686984161281305242L,
            -5038936143766954517L,
            -7720497729755473937L,
            -1284749923383027329L,
            -4717148753448332187L,
            -7463067817500576073L,
            -872862063775190746L,
            -4387638465762062920L,
            -7199459587351560659L,
            -451088895536766085L,
            -4050219931171323192L,
            -6929524759678968877L,
            -19193171260619233L,
            -3704703351750405709L,
            -6653111496142234891L,
            -9011838011655698236L,
            -3350894374423386208L,
            -6370064314280619289L,
            -8785400266166405755L,
            -2988593981640518238L,
            -6080224000054324913L,
            -8553528014785370254L,
            -2617598379430861437L,
            -5783427518286599473L,
            -8316090829371189901L,
            -2237698882768172872L,
            -5479507920956448621L,
            -8072955151507069220L,
            -1848681798185579782L,
            -5168294253290374149L,
            -7823984217374209643L,
            -1450328303573004458L,
            -4849611457600313890L,
            -7569037980822161435L,
            -1042414325089727327L,
            -4523280274813692185L,
            -7307973034592864071L,
            -624710411122851544L,
            -4189117143640191558L,
            -7040642529654063570L,
            -196981603220770742L,
            -3846934097318526917L,
            -6766896092596731857L,
            -9102865688819295809L,
            -3496538657885142324L,
            -6486579741050024183L,
            -8878612607581929669L,
            -3137733727905356501L,
            -6199535797066195524L,
            -8648977452394866743L,
            -2770317479606055818L,
            -5905602798426754978L,
            -8413831053483314306L,
            -2394083241347571919L,
            -5604615407819967859L,
            -8173041140997884610L,
            -2008819381370884406L,
            -5296404319838617848L,
            -7926472270612804602L,
            -1614309188754756393L,
            -4980796165745715438L,
            -7673985747338482674L,
            -1210330751515841308L,
            -4657613415954583370L,
            -7415439547505577019L,
            -796656831783192261L,
            -4326674280168464132L,
            -7150688238876681629L,
            -373054737976959636L,
            -3987792605123478032L,
            -6879582898840692749L,
            -9193015133814464522L,
            -3640777769877412266L,
            -6601971030643840136L,
            -8970925639256982432L,
            -3285434578585440922L,
            -6317696477610263061L,
            -8743505996830120772L,
            -2921563150702462265L,
            -6026599335303880135L,
            -8510628282985014432L,
            -2548958808550292121L,
            -5728515861582144020L,
            -8272161504007625539L,
            -2167411962186469893L,
            -5423278384491086237L,
            -8027971522334779313L,
            -1776707991509915931L,
            -5110715207949843068L,
            -7777920981101784778L,
            -1376627125537124675L,
            -4790650515171610063L,
            -7521869226879198374L,
            -966944318780986428L,
            -4462904269766699466L,
            -7259672230555269896L,
            -547429124662700864L,
            -4127292114472071014L,
            -6991182506319567135L,
            -117845565885576446L,
            -3783625267450371480L,
            -6716249028702207507L,
            -9062348037703676329L,
            -3431710416100151157L,
            -6434717147622031249L,
            -8837122532839535322L,
            -3071349608317525546L,
            -6146428501395930760L,
            -8606491615858654931L,
            -2702340141148116920L,
            -5851220927660403859L,
            -8370325556870233411L,
            -2324474446766642487L,
            -5548928372155224313L,
            -8128491512466089774L,
            -1937539975720012668L,
            -5239380795317920458L,
            -7880853450996246689L,
            -1541319077368263733L,
            -4922404076636521310L,
            -7627272076051127371L,
            -1135588877456072824L,
            -4597819916706768583L,
            -7367604748107325189L,
            -720121152745989333L,
            -4265445736938701790L,
            -7101705404292871755L,
            -294682202642863838L,
            -3925094576856201394L,
            -6829424476226871438L,
            -9152888395723407474L,
            -3576574988931720989L,
            -6550608805887287114L,
            -8929835859451740015L,
            -3219690930897053053L,
            -6265101559459552766L,
            -8701430062309552536L,
            -2854241655469553088L,
            -5972742139117552794L,
            -8467542526035952558L,
            -2480021597431793123L,
            -5673366092687344822L,
            -8228041688891786181L,
            -2096820258001126919L,
            -5366805021142811859L,
            -7982792831656159810L,
            -1704422086424124727L,
            -5052886483881210105L,
            -7731658001846878407L,
            -1302606358729274481L,
            -4731433901725329908L,
            -7474495936122174250L,
            -891147053569747830L,
            -4402266457597708587L,
            -7211161980820077193L,
            -469812725086392539L,
            -4065198994811024355L,
            -6941508010590729807L,
            -38366372719436721L,
            -3720041912917459700L,
            -6665382345075878084L,
            -9021654690802612790L,
            -3366601061058449494L,
            -6382629663588669919L,
            -8795452545612846258L,
            -3004677628754823043L,
            -6093090917745768758L,
            -8563821548938525330L,
            -2634068034075909558L,
            -5796603242002637969L,
            -8326631408344020699L,
            -2254563809124702148L,
            -5492999862041672042L,
            -8083748704375247957L,
            -1865951482774665761L,
            -5182110000961642932L,
            -7835036815511224669L,
            -1468012460592228501L,
            -4863758783215693124L,
            -7580355841314464822L,
            -1060522901877412746L,
            -4537767136243840520L,
            -7319562523736982739L,
            -643253593753441413L,
            -4203951689744663454L,
            -7052510166537641086L,
            -215969822234494768L,
            -3862124672529506138L,
            -6779048552765515233L,
            -9112587656954322510L,
            -3512093806901185046L,
            -6499023860262858360L,
            -8888567902952197011L,
            -3153662200497784248L,
            -6212278575140137722L,
            -8659171674854020501L,
            -2786628235540701832L,
            -5918651403174471789L,
            -8424269937281487754L,
            -2410785455424649437L,
            -5617977179081629873L,
            -8183730558007214222L,
            -2025922448585811785L,
            -5310086773610559751L,
            -7937418233630358124L,
            -1631822729582842029L,
            -4994806998408183946L,
            -7685194413468457480L,
            -1228264617323800998L,
            -4671960508600951122L,
            -7426917221622671221L,
            -815021110370542984L,
            -4341365703038344710L,
            -7162441377172586091L,
    };

    private static int[] exp10Table = {
            2118,
            2115,
            2111,
            2108,
            2105,
            2101,
            2098,
            2095,
            2091,
            2088,
            2085,
            2081,
            2078,
            2075,
            2071,
            2068,
            2065,
            2061,
            2058,
            2055,
            2052,
            2048,
            2045,
            2042,
            2038,
            2035,
            2032,
            2028,
            2025,
            2022,
            2018,
            2015,
            2012,
            2008,
            2005,
            2002,
            1998,
            1995,
            1992,
            1988,
            1985,
            1982,
            1978,
            1975,
            1972,
            1968,
            1965,
            1962,
            1958,
            1955,
            1952,
            1949,
            1945,
            1942,
            1939,
            1935,
            1932,
            1929,
            1925,
            1922,
            1919,
            1915,
            1912,
            1909,
            1905,
            1902,
            1899,
            1895,
            1892,
            1889,
            1885,
            1882,
            1879,
            1875,
            1872,
            1869,
            1865,
            1862,
            1859,
            1856,
            1852,
            1849,
            1846,
            1842,
            1839,
            1836,
            1832,
            1829,
            1826,
            1822,
            1819,
            1816,
            1812,
            1809,
            1806,
            1802,
            1799,
            1796,
            1792,
            1789,
            1786,
            1782,
            1779,
            1776,
            1772,
            1769,
            1766,
            1762,
            1759,
            1756,
            1753,
            1749,
            1746,
            1743,
            1739,
            1736,
            1733,
            1729,
            1726,
            1723,
            1719,
            1716,
            1713,
            1709,
            1706,
            1703,
            1699,
            1696,
            1693,
            1689,
            1686,
            1683,
            1679,
            1676,
            1673,
            1669,
            1666,
            1663,
            1660,
            1656,
            1653,
            1650,
            1646,
            1643,
            1640,
            1636,
            1633,
            1630,
            1626,
            1623,
            1620,
            1616,
            1613,
            1610,
            1606,
            1603,
            1600,
            1596,
            1593,
            1590,
            1586,
            1583,
            1580,
            1576,
            1573,
            1570,
            1567,
            1563,
            1560,
            1557,
            1553,
            1550,
            1547,
            1543,
            1540,
            1537,
            1533,
            1530,
            1527,
            1523,
            1520,
            1517,
            1513,
            1510,
            1507,
            1503,
            1500,
            1497,
            1493,
            1490,
            1487,
            1483,
            1480,
            1477,
            1473,
            1470,
            1467,
            1464,
            1460,
            1457,
            1454,
            1450,
            1447,
            1444,
            1440,
            1437,
            1434,
            1430,
            1427,
            1424,
            1420,
            1417,
            1414,
            1410,
            1407,
            1404,
            1400,
            1397,
            1394,
            1390,
            1387,
            1384,
            1380,
            1377,
            1374,
            1371,
            1367,
            1364,
            1361,
            1357,
            1354,
            1351,
            1347,
            1344,
            1341,
            1337,
            1334,
            1331,
            1327,
            1324,
            1321,
            1317,
            1314,
            1311,
            1307,
            1304,
            1301,
            1297,
            1294,
            1291,
            1287,
            1284,
            1281,
            1277,
            1274,
            1271,
            1268,
            1264,
            1261,
            1258,
            1254,
            1251,
            1248,
            1244,
            1241,
            1238,
            1234,
            1231,
            1228,
            1224,
            1221,
            1218,
            1214,
            1211,
            1208,
            1204,
            1201,
            1198,
            1194,
            1191,
            1188,
            1184,
            1181,
            1178,
            1175,
            1171,
            1168,
            1165,
            1161,
            1158,
            1155,
            1151,
            1148,
            1145,
            1141,
            1138,
            1135,
            1131,
            1128,
            1125,
            1121,
            1118,
            1115,
            1111,
            1108,
            1105,
            1101,
            1098,
            1095,
            1091,
            1088,
            1085,
            1082,
            1078,
            1075,
            1072,
            1068,
            1065,
            1062,
            1058,
            1055,
            1052,
            1048,
            1045,
            1042,
            1038,
            1035,
            1032,
            1028,
            1025,
            1022,
            1018,
            1015,
            1012,
            1008,
            1005,
            1002,
            998,
            995,
            992,
            988,
            985,
            982,
            979,
            975,
            972,
            969,
            965,
            962,
            959,
            955,
            952,
            949,
            945,
            942,
            939,
            935,
            932,
            929,
            925,
            922,
            919,
            915,
            912,
            909,
            905,
            902,
            899,
            895,
            892,
            889,
            886,
            882,
            879,
            876,
            872,
            869,
            866,
            862,
            859,
            856,
            852,
            849,
            846,
            842,
            839,
            836,
            832,
            829,
            826,
            822,
            819,
            816,
            812,
            809,
            806,
            802,
            799,
            796,
            792,
            789,
            786,
            783,
            779,
            776,
            773,
            769,
            766,
            763,
            759,
            756,
            753,
            749,
            746,
            743,
            739,
            736,
            733,
            729,
            726,
            723,
            719,
            716,
            713,
            709,
            706,
            703,
            699,
            696,
            693,
            690,
            686,
            683,
            680,
            676,
            673,
            670,
            666,
            663,
            660,
            656,
            653,
            650,
            646,
            643,
            640,
            636,
            633,
            630,
            626,
            623,
            620,
            616,
            613,
            610,
            606,
            603,
            600,
            596,
            593,
            590,
            587,
            583,
            580,
            577,
            573,
            570,
            567,
            563,
            560,
            557,
            553,
            550,
            547,
            543,
            540,
            537,
            533,
            530,
            527,
            523,
            520,
            517,
            513,
            510,
            507,
            503,
            500,
            497,
            494,
            490,
            487,
            484,
            480,
            477,
            474,
            470,
            467,
            464,
            460,
            457,
            454,
            450,
            447,
            444,
            440,
            437,
            434,
            430,
            427,
            424,
            420,
            417,
            414,
            410,
            407,
            404,
            401,
            397,
            394,
            391,
            387,
            384,
            381,
            377,
            374,
            371,
            367,
            364,
            361,
            357,
            354,
            351,
            347,
            344,
            341,
            337,
            334,
            331,
            327,
            324,
            321,
            317,
            314,
            311,
            307,
            304,
            301,
            298,
            294,
            291,
            288,
            284,
            281,
            278,
            274,
            271,
            268,
            264,
            261,
            258,
            254,
            251,
            248,
            244,
            241,
            238,
            234,
            231,
            228,
            224,
            221,
            218,
            214,
            211,
            208,
            205,
            201,
            198,
            195,
            191,
            188,
            185,
            181,
            178,
            175,
            171,
            168,
            165,
            161,
            158,
            155,
            151,
            148,
            145,
            141,
            138,
            135,
            131,
            128,
            125,
            121,
            118,
            115,
            111,
            108,
            105,
            102,
            98,
            95,
            92,
            88,
            85,
            82,
            78,
            75,
            72,
            68,
            65,
            62,
            58,
            55,
            52,
            48,
            45,
            42,
            38,
            35,
            32,
            28,
            25,
            22,
            18,
            15,
            12,
            9,
            5,
            2,
            -1,
            -5,
            -8,
            -11,
            -15,
            -18,
            -21,
            -25,
            -28,
            -31,
            -35,
            -38,
            -41,
            -45,
            -48,
            -51,
            -55,
            -58,
            -61,
            -65,
            -68,
            -71,
    };
}