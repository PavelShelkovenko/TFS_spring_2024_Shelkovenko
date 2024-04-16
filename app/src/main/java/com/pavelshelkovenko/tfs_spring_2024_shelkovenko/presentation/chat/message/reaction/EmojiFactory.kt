package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.reaction

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Emoji

object EmojiFactory {

    fun getEmojiList(): List<Emoji> {
        return emojiList
    }

    private val emojiList = listOf(

// Smileys & Emotion
        Emoji("grinning", "1f600"),
        Emoji("smiley", "1f603"),
        Emoji("big_smile", "1f604"),
        Emoji("grinning_face_with_smiling_eyes", "1f601"),
        Emoji("laughing", "1f606"),
        Emoji("sweat_smile", "1f605"),
        Emoji("rolling_on_the_floor_laughing", "1f923"),
        Emoji("joy", "1f602"),
        Emoji("smile", "1f642"),
        Emoji("upside_down", "1f643"),
        Emoji("wink", "1f609"),
        Emoji("blush", "1f60a"),
        Emoji("innocent", "1f607"),
        Emoji("heart_eyes", "1f60d"),
        Emoji("heart_kiss", "1f618"),
        Emoji("kiss", "1f617"),
        Emoji("smiling_face", "263a"),
        Emoji("kiss_with_blush", "1f61a"),
        Emoji("kiss_smiling_eyes", "1f619"),
        Emoji("yum", "1f60b"),
        Emoji("stuck_out_tongue", "1f61b"),
        Emoji("stuck_out_tongue_wink", "1f61c"),
        Emoji("stuck_out_tongue_closed_eyes", "1f61d"),
        Emoji("money_face", "1f911"),
        Emoji("hug", "1f917"),
        Emoji("thinking", "1f914"),
        Emoji("silence", "1f910"),
        Emoji("neutral", "1f610"),
        Emoji("expressionless", "1f611"),
        Emoji("speechless", "1f636"),
        Emoji("smirk", "1f60f"),
        Emoji("unamused", "1f612"),
        Emoji("rolling_eyes", "1f644"),
        Emoji("grimacing", "1f62c"),
        Emoji("lying", "1f925"),
        Emoji("relieved", "1f60c"),
        Emoji("pensive", "1f614"),
        Emoji("sleepy", "1f62a"),
        Emoji("drooling", "1f924"),
        Emoji("sleeping", "1f634"),
        Emoji("mask", "1f637"),
        Emoji("sick", "1f912"),
        Emoji("hurt", "1f915"),
        Emoji("nauseated", "1f922"),
        Emoji("sneezing", "1f927"),
        Emoji("dizzy", "1f635"),
        Emoji("cowboy", "1f920"),
        Emoji("sunglasses", "1f60e"),
        Emoji("nerd", "1f913"),
        Emoji("oh_no", "1f615"),
        Emoji("worried", "1f61f"),
        Emoji("frown", "1f641"),
        Emoji("sad", "2639"),
        Emoji("open_mouth", "1f62e"),
        Emoji("hushed", "1f62f"),
        Emoji("astonished", "1f632"),
        Emoji("flushed", "1f633"),
        Emoji("frowning", "1f626"),
        Emoji("anguished", "1f627"),
        Emoji("fear", "1f628"),
        Emoji("cold_sweat", "1f630"),
        Emoji("exhausted", "1f625"),
        Emoji("cry", "1f622"),
        Emoji("sob", "1f62d"),
        Emoji("scream", "1f631"),
        Emoji("confounded", "1f616"),
        Emoji("persevere", "1f623"),
        Emoji("disappointed", "1f61e"),
        Emoji("sweat", "1f613"),
        Emoji("weary", "1f629"),
        Emoji("anguish", "1f62b"),
        Emoji("triumph", "1f624"),
        Emoji("rage", "1f621"),
        Emoji("angry", "1f620"),
        Emoji("smiling_devil", "1f608"),
        Emoji("devil", "1f47f"),
        Emoji("skull", "1f480"),
        Emoji("skull_and_crossbones", "2620"),
        Emoji("poop", "1f4a9"),
        Emoji("clown", "1f921"),
        Emoji("ogre", "1f479"),
        Emoji("goblin", "1f47a"),
        Emoji("ghost", "1f47b"),
        Emoji("alien", "1f47d"),
        Emoji("space_invader", "1f47e"),
        Emoji("robot", "1f916"),
        Emoji("smiley_cat", "1f63a"),
        Emoji("smile_cat", "1f638"),
        Emoji("joy_cat", "1f639"),
        Emoji("heart_eyes_cat", "1f63b"),
        Emoji("smirk_cat", "1f63c"),
        Emoji("kissing_cat", "1f63d"),
        Emoji("scream_cat", "1f640"),
        Emoji("crying_cat", "1f63f"),
        Emoji("angry_cat", "1f63e"),
        Emoji("see_no_evil", "1f648"),
        Emoji("hear_no_evil", "1f649"),
        Emoji("speak_no_evil", "1f64a"),
        Emoji("lipstick_kiss", "1f48b"),
        Emoji("love_letter", "1f48c"),
        Emoji("cupid", "1f498"),
        Emoji("gift_heart", "1f49d"),
        Emoji("sparkling_heart", "1f496"),
        Emoji("heart_pulse", "1f497"),
        Emoji("heartbeat", "1f493"),
        Emoji("revolving_hearts", "1f49e"),
        Emoji("two_hearts", "1f495"),
        Emoji("heart_box", "1f49f"),
        Emoji("heart_exclamation", "2763"),
        Emoji("broken_heart", "1f494"),
        Emoji("heart", "2764"),
        Emoji("yellow_heart", "1f49b"),
        Emoji("green_heart", "1f49a"),
        Emoji("blue_heart", "1f499"),
        Emoji("purple_heart", "1f49c"),
        Emoji("black_heart", "1f5a4"),
        Emoji("100", "1f4af"),
        Emoji("anger", "1f4a2"),
        Emoji("boom", "1f4a5"),
        Emoji("seeing_stars", "1f4ab"),
        Emoji("sweat_drops", "1f4a6"),
        Emoji("dash", "1f4a8"),
        Emoji("hole", "1f573"),
        Emoji("bomb", "1f4a3"),
        Emoji("umm", "1f4ac"),
        Emoji("speech_bubble", "1f5e8"),
        Emoji("anger_bubble", "1f5ef"),
        Emoji("thought", "1f4ad"),
        Emoji("zzz", "1f4a4"),

// PeopleBody
        Emoji("wave", "1f44b"),
        Emoji("stop", "1f91a"),
        Emoji("high_five", "1f590"),
        Emoji("hand", "270b"),
        Emoji("spock", "1f596"),
        Emoji("ok", "1f44c"),
        Emoji("peace_sign", "270c"),
        Emoji("fingers_crossed", "1f91e"),
        Emoji("rock_on", "1f918"),
        Emoji("call_me", "1f919"),
        Emoji("point_left", "1f448"),
        Emoji("point_right", "1f449"),
        Emoji("point_up", "1f446"),
        Emoji("middle_finger", "1f595"),
        Emoji("point_down", "1f447"),
        Emoji("wait_one_second", "261d"),
        Emoji("+1", "1f44d"),
        Emoji("-1", "1f44e"),
        Emoji("fist", "270a"),
        Emoji("fist_bump", "1f44a"),
        Emoji("left_fist", "1f91b"),
        Emoji("right_fist", "1f91c"),
        Emoji("clap", "1f44f"),
        Emoji("raised_hands", "1f64c"),
        Emoji("open_hands", "1f450"),
        Emoji("handshake", "1f91d"),
        Emoji("pray", "1f64f"),
        Emoji("writing", "270d"),
        Emoji("nail_polish", "1f485"),
        Emoji("selfie", "1f933"),
        Emoji("muscle", "1f4aa"),
        Emoji("ear", "1f442"),
        Emoji("nose", "1f443"),
        Emoji("eyes", "1f440"),
        Emoji("eye", "1f441"),
        Emoji("tongue", "1f445"),
        Emoji("lips", "1f444"),
        Emoji("baby", "1f476"),
        Emoji("boy", "1f466"),
        Emoji("girl", "1f467"),
        Emoji("man", "1f468"),
        Emoji("woman", "1f469"),
        Emoji("older_man", "1f474"),
        Emoji("older_woman", "1f475"),
        Emoji("person_frowning", "1f64d"),
        Emoji("person_pouting", "1f64e"),
        Emoji("no_signal", "1f645"),
        Emoji("ok_signal", "1f646"),
        Emoji("information_desk_person", "1f481"),
        Emoji("raising_hand", "1f64b"),
        Emoji("bow", "1f647"),
        Emoji("face_palm", "1f926"),
        Emoji("shrug", "1f937"),
        Emoji("police", "1f46e"),
        Emoji("detective", "1f575"),
        Emoji("guard", "1f482"),
        Emoji("construction_worker", "1f477"),
        Emoji("prince", "1f934"),
        Emoji("princess", "1f478"),
        Emoji("turban", "1f473"),
        Emoji("gua_pi_mao", "1f472"),
        Emoji("bride", "1f470"),
        Emoji("pregnant", "1f930"),
        Emoji("angel", "1f47c"),
        Emoji("santa", "1f385"),
        Emoji("mother_christmas", "1f936"),
        Emoji("massage", "1f486"),
        Emoji("haircut", "1f487"),
        Emoji("walking", "1f6b6"),
        Emoji("running", "1f3c3"),
        Emoji("dancer", "1f483"),
        Emoji("dancing", "1f57a"),
        Emoji("levitating", "1f574"),
        Emoji("dancers", "1f46f"),
        Emoji("fencing", "1f93a"),
        Emoji("horse_racing", "1f3c7"),
        Emoji("skier", "26f7"),
        Emoji("snowboarder", "1f3c2"),
        Emoji("golf", "1f3cc"),
        Emoji("surf", "1f3c4"),
        Emoji("rowboat", "1f6a3"),
        Emoji("swim", "1f3ca"),
        Emoji("ball", "26f9"),
        Emoji("lift", "1f3cb"),
        Emoji("cyclist", "1f6b4"),
        Emoji("mountain_biker", "1f6b5"),
        Emoji("cartwheel", "1f938"),
        Emoji("wrestling", "1f93c"),
        Emoji("water_polo", "1f93d"),
        Emoji("handball", "1f93e"),
        Emoji("juggling", "1f939"),
        Emoji("bath", "1f6c0"),
        Emoji("in_bed", "1f6cc"),
        Emoji("two_women_holding_hands", "1f46d"),
        Emoji("man_and_woman_holding_hands", "1f46b"),
        Emoji("two_men_holding_hands", "1f46c"),
        Emoji("family", "1f46a"),
        Emoji("speaking_head", "1f5e3"),
        Emoji("silhouette", "1f464"),
        Emoji("silhouettes", "1f465"),
        Emoji("footprints", "1f463"),
        Emoji("tuxedo", "1f935"),

// Animal Nature
        Emoji("monkey_face", "1f435"),
        Emoji("monkey", "1f412"),
        Emoji("gorilla", "1f98d"),
        Emoji("puppy", "1f436"),
        Emoji("dog", "1f415"),
        Emoji("poodle", "1f429"),
        Emoji("wolf", "1f43a"),
        Emoji("fox", "1f98a"),
        Emoji("kitten", "1f431"),
        Emoji("cat", "1f408"),
        Emoji("lion", "1f981"),
        Emoji("tiger_cub", "1f42f"),
        Emoji("tiger", "1f405"),
        Emoji("leopard", "1f406"),
        Emoji("pony", "1f434"),
        Emoji("horse", "1f40e"),
        Emoji("unicorn", "1f984"),
        Emoji("deer", "1f98c"),
        Emoji("calf", "1f42e"),
        Emoji("ox", "1f402"),
        Emoji("water_buffalo", "1f403"),
        Emoji("cow", "1f404"),
        Emoji("piglet", "1f437"),
        Emoji("pig", "1f416"),
        Emoji("boar", "1f417"),
        Emoji("pig_nose", "1f43d"),
        Emoji("ram", "1f40f"),
        Emoji("sheep", "1f411"),
        Emoji("goat", "1f410"),
        Emoji("arabian_camel", "1f42a"),
        Emoji("camel", "1f42b"),
        Emoji("elephant", "1f418"),
        Emoji("rhinoceros", "1f98f"),
        Emoji("dormouse", "1f42d"),
        Emoji("mouse", "1f401"),
        Emoji("rat", "1f400"),
        Emoji("hamster", "1f439"),
        Emoji("bunny", "1f430"),
        Emoji("rabbit", "1f407"),
        Emoji("chipmunk", "1f43f"),
        Emoji("bat", "1f987"),
        Emoji("bear", "1f43b"),
        Emoji("koala", "1f428"),
        Emoji("panda", "1f43c"),
        Emoji("paw_prints", "1f43e"),
        Emoji("turkey", "1f983"),
        Emoji("chicken", "1f414"),
        Emoji("rooster", "1f413"),
        Emoji("hatching", "1f423"),
        Emoji("chick", "1f424"),
        Emoji("new_baby", "1f425"),
        Emoji("bird", "1f426"),
        Emoji("penguin", "1f427"),
        Emoji("dove", "1f54a"),
        Emoji("eagle", "1f985"),
        Emoji("duck", "1f986"),
        Emoji("owl", "1f989"),
        Emoji("frog", "1f438"),
        Emoji("crocodile", "1f40a"),
        Emoji("turtle", "1f422"),
        Emoji("lizard", "1f98e"),
        Emoji("snake", "1f40d"),
        Emoji("dragon_face", "1f432"),
        Emoji("dragon", "1f409"),
        Emoji("whale", "1f433"),
        Emoji("humpback_whale", "1f40b"),
        Emoji("dolphin", "1f42c"),
        Emoji("fish", "1f41f"),
        Emoji("tropical_fish", "1f420"),
        Emoji("blowfish", "1f421"),
        Emoji("shark", "1f988"),
        Emoji("octopus", "1f419"),
        Emoji("shell", "1f41a"),
        Emoji("snail", "1f40c"),
        Emoji("butterfly", "1f98b"),
        Emoji("bug", "1f41b"),
        Emoji("ant", "1f41c"),
        Emoji("bee", "1f41d"),
        Emoji("spider", "1f577"),
        Emoji("web", "1f578"),
        Emoji("scorpion", "1f982"),
        Emoji("bouquet", "1f490"),
        Emoji("cherry_blossom", "1f338"),
        Emoji("white_flower", "1f4ae"),
        Emoji("rosette", "1f3f5"),
        Emoji("rose", "1f339"),
        Emoji("wilted_flower", "1f940"),
        Emoji("hibiscus", "1f33a"),
        Emoji("sunflower", "1f33b"),
        Emoji("blossom", "1f33c"),
        Emoji("tulip", "1f337"),
        Emoji("seedling", "1f331"),
        Emoji("evergreen_tree", "1f332"),
        Emoji("tree", "1f333"),
        Emoji("palm_tree", "1f334"),
        Emoji("cactus", "1f335"),
        Emoji("harvest", "1f33e"),
        Emoji("herb", "1f33f"),
        Emoji("shamrock", "2618"),
        Emoji("lucky", "1f340"),
        Emoji("maple_leaf", "1f341"),
        Emoji("fallen_leaf", "1f342"),
        Emoji("leaves", "1f343"),
        Emoji("beetle", "1f41e"),

// Food &ink
        Emoji("grapes", "1f347"),
        Emoji("melon", "1f348"),
        Emoji("watermelon", "1f349"),
        Emoji("orange", "1f34a"),
        Emoji("lemon", "1f34b"),
        Emoji("banana", "1f34c"),
        Emoji("pineapple", "1f34d"),
        Emoji("apple", "1f34e"),
        Emoji("green_apple", "1f34f"),
        Emoji("pear", "1f350"),
        Emoji("peach", "1f351"),
        Emoji("cherries", "1f352"),
        Emoji("strawberry", "1f353"),
        Emoji("kiwi", "1f95d"),
        Emoji("tomato", "1f345"),
        Emoji("avocado", "1f951"),
        Emoji("eggplant", "1f346"),
        Emoji("potato", "1f954"),
        Emoji("carrot", "1f955"),
        Emoji("corn", "1f33d"),
        Emoji("hot_pepper", "1f336"),
        Emoji("cucumber", "1f952"),
        Emoji("mushroom", "1f344"),
        Emoji("peanuts", "1f95c"),
        Emoji("chestnut", "1f330"),
        Emoji("bread", "1f35e"),
        Emoji("croissant", "1f950"),
        Emoji("baguette", "1f956"),
        Emoji("pancakes", "1f95e"),
        Emoji("cheese", "1f9c0"),
        Emoji("meat", "1f356"),
        Emoji("drumstick", "1f357"),
        Emoji("bacon", "1f953"),
        Emoji("hamburger", "1f354"),
        Emoji("fries", "1f35f"),
        Emoji("pizza", "1f355"),
        Emoji("hotdog", "1f32d"),
        Emoji("taco", "1f32e"),
        Emoji("burrito", "1f32f"),
        Emoji("doner_kebab", "1f959"),
        Emoji("egg", "1f95a"),
        Emoji("cooking", "1f373"),
        Emoji("paella", "1f958"),
        Emoji("food", "1f372"),
        Emoji("salad", "1f957"),
        Emoji("popcorn", "1f37f"),
        Emoji("bento", "1f371"),
        Emoji("senbei", "1f358"),
        Emoji("onigiri", "1f359"),
        Emoji("rice", "1f35a"),
        Emoji("curry", "1f35b"),
        Emoji("ramen", "1f35c"),
        Emoji("spaghetti", "1f35d"),
        Emoji("yam", "1f360"),
        Emoji("oden", "1f362"),
        Emoji("sushi", "1f363"),
        Emoji("tempura", "1f364"),
        Emoji("naruto", "1f365"),
        Emoji("dango", "1f361"),
        Emoji("crab", "1f980"),
        Emoji("shrimp", "1f990"),
        Emoji("squid", "1f991"),
        Emoji("soft_serve", "1f366"),
        Emoji("shaved_ice", "1f367"),
        Emoji("ice_cream", "1f368"),
        Emoji("donut", "1f369"),
        Emoji("cookie", "1f36a"),
        Emoji("birthday", "1f382"),
        Emoji("cake", "1f370"),
        Emoji("chocolate", "1f36b"),
        Emoji("candy", "1f36c"),
        Emoji("lollipop", "1f36d"),
        Emoji("custard", "1f36e"),
        Emoji("honey", "1f36f"),
        Emoji("baby_bottle", "1f37c"),
        Emoji("milk", "1f95b"),
        Emoji("coffee", "2615"),
        Emoji("tea", "1f375"),
        Emoji("sake", "1f376"),
        Emoji("champagne", "1f37e"),
        Emoji("wine", "1f377"),
        Emoji("cocktail", "1f378"),
        Emoji("tropical_drink", "1f379"),
        Emoji("beer", "1f37a"),
        Emoji("beers", "1f37b"),
        Emoji("clink", "1f942"),
        Emoji("small_glass", "1f943"),
        Emoji("hungry", "1f37d"),
        Emoji("fork_and_knife", "1f374"),
        Emoji("spoon", "1f944"),
        Emoji("knife", "1f52a"),
        Emoji("vase", "1f3fa"),

// Activis
        Emoji("jack-o-lantern", "1f383"),
        Emoji("holiday_tree", "1f384"),
        Emoji("fireworks", "1f386"),
        Emoji("sparkler", "1f387"),
        Emoji("sparkles", "2728"),
        Emoji("balloon", "1f388"),
        Emoji("tada", "1f389"),
        Emoji("confetti", "1f38a"),
        Emoji("wish_tree", "1f38b"),
        Emoji("bamboo", "1f38d"),
        Emoji("dolls", "1f38e"),
        Emoji("carp_streamer", "1f38f"),
        Emoji("wind_chime", "1f390"),
        Emoji("moon_ceremony", "1f391"),
        Emoji("ribbon", "1f380"),
        Emoji("gift", "1f381"),
        Emoji("reminder_ribbon", "1f397"),
        Emoji("ticket", "1f39f"),
        Emoji("pass", "1f3ab"),
        Emoji("military_medal", "1f396"),
        Emoji("trophy", "1f3c6"),
        Emoji("medal", "1f3c5"),
        Emoji("first_place", "1f947"),
        Emoji("second_place", "1f948"),
        Emoji("third_place", "1f949"),
        Emoji("football", "26bd"),
        Emoji("baseball", "26be"),
        Emoji("basketball", "1f3c0"),
        Emoji("volleyball", "1f3d0"),
        Emoji("american_football", "1f3c8"),
        Emoji("rugby", "1f3c9"),
        Emoji("tennis", "1f3be"),
        Emoji("strike", "1f3b3"),
        Emoji("cricket", "1f3cf"),
        Emoji("field_hockey", "1f3d1"),
        Emoji("ice_hockey", "1f3d2"),
        Emoji("ping_pong", "1f3d3"),
        Emoji("badminton", "1f3f8"),
        Emoji("boxing_glove", "1f94a"),
        Emoji("black_belt", "1f94b"),
        Emoji("gooooooooal", "1f945"),
        Emoji("hole_in_one", "26f3"),
        Emoji("ice_skate", "26f8"),
        Emoji("fishing", "1f3a3"),
        Emoji("running_shirt", "1f3bd"),
        Emoji("ski", "1f3bf"),
        Emoji("direct_hit", "1f3af"),
        Emoji("billiards", "1f3b1"),
        Emoji("crystal_ball", "1f52e"),
        Emoji("video_game", "1f3ae"),
        Emoji("joystick", "1f579"),
        Emoji("slot_machine", "1f3b0"),
        Emoji("dice", "1f3b2"),
        Emoji("spades", "2660"),
        Emoji("hearts", "2665"),
        Emoji("diamonds", "2666"),
        Emoji("clubs", "2663"),
        Emoji("joker", "1f0cf"),
        Emoji("mahjong", "1f004"),
        Emoji("playing_cards", "1f3b4"),
        Emoji("performing_arts", "1f3ad"),
        Emoji("picture", "1f5bc"),
        Emoji("art", "1f3a8"),

// TravelPlaces
        Emoji("earth_africa", "1f30d"),
        Emoji("earth_americas", "1f30e"),
        Emoji("earth_asia", "1f30f"),
        Emoji("www", "1f310"),
        Emoji("map", "1f5fa"),
        Emoji("japan", "1f5fe"),
        Emoji("snowy_mountain", "1f3d4"),
        Emoji("mountain", "26f0"),
        Emoji("volcano", "1f30b"),
        Emoji("mount_fuji", "1f5fb"),
        Emoji("campsite", "1f3d5"),
        Emoji("beach", "1f3d6"),
        Emoji("desert", "1f3dc"),
        Emoji("island", "1f3dd"),
        Emoji("national_park", "1f3de"),
        Emoji("stadium", "1f3df"),
        Emoji("classical_building", "1f3db"),
        Emoji("construction", "1f3d7"),
        Emoji("houses", "1f3d8"),
        Emoji("derelict_house", "1f3da"),
        Emoji("house", "1f3e0"),
        Emoji("suburb", "1f3e1"),
        Emoji("office", "1f3e2"),
        Emoji("japan_post", "1f3e3"),
        Emoji("post_office", "1f3e4"),
        Emoji("hospital", "1f3e5"),
        Emoji("bank", "1f3e6"),
        Emoji("hotel", "1f3e8"),
        Emoji("love_hotel", "1f3e9"),
        Emoji("convenience_store", "1f3ea"),
        Emoji("school", "1f3eb"),
        Emoji("department_store", "1f3ec"),
        Emoji("factory", "1f3ed"),
        Emoji("shiro", "1f3ef"),
        Emoji("castle", "1f3f0"),
        Emoji("wedding", "1f492"),
        Emoji("tower", "1f5fc"),
        Emoji("statue", "1f5fd"),
        Emoji("church", "26ea"),
        Emoji("mosque", "1f54c"),
        Emoji("synagogue", "1f54d"),
        Emoji("shinto_shrine", "26e9"),
        Emoji("kaaba", "1f54b"),
        Emoji("fountain", "26f2"),
        Emoji("tent", "26fa"),
        Emoji("foggy", "1f301"),
        Emoji("night", "1f303"),
        Emoji("city", "1f3d9"),
        Emoji("mountain_sunrise", "1f304"),
        Emoji("sunrise", "1f305"),
        Emoji("sunset", "1f306"),
        Emoji("city_sunrise", "1f307"),
        Emoji("bridge", "1f309"),
        Emoji("hot_springs", "2668"),
        Emoji("carousel", "1f3a0"),
        Emoji("ferris_wheel", "1f3a1"),
        Emoji("roller_coaster", "1f3a2"),
        Emoji("barber", "1f488"),
        Emoji("circus", "1f3aa"),
        Emoji("train", "1f682"),
        Emoji("railway_car", "1f683"),
        Emoji("high_speed_train", "1f684"),
        Emoji("bullet_train", "1f685"),
        Emoji("oncoming_train", "1f686"),
        Emoji("subway", "1f687"),
        Emoji("light_rail", "1f688"),
        Emoji("station", "1f689"),
        Emoji("oncoming_tram", "1f68a"),
        Emoji("monorail", "1f69d"),
        Emoji("mountain_railway", "1f69e"),
        Emoji("tram", "1f68b"),
        Emoji("bus", "1f68c"),
        Emoji("oncoming_bus", "1f68d"),
        Emoji("trolley", "1f68e"),
        Emoji("minibus", "1f690"),
        Emoji("ambulance", "1f691"),
        Emoji("fire_truck", "1f692"),
        Emoji("police_car", "1f693"),
        Emoji("oncoming_police_car", "1f694"),
        Emoji("taxi", "1f695"),
        Emoji("oncoming_taxi", "1f696"),
        Emoji("car", "1f697"),
        Emoji("oncoming_car", "1f698"),
        Emoji("recreational_vehicle", "1f699"),
        Emoji("moving_truck", "1f69a"),
        Emoji("truck", "1f69b"),
        Emoji("tractor", "1f69c"),
        Emoji("racecar", "1f3ce"),
        Emoji("motorcycle", "1f3cd"),
        Emoji("scooter", "1f6f5"),
        Emoji("bike", "1f6b2"),
        Emoji("kick_scooter", "1f6f4"),
        Emoji("bus_stop", "1f68f"),
        Emoji("road", "1f6e3"),
        Emoji("railway_track", "1f6e4"),
        Emoji("oil_drum", "1f6e2"),
        Emoji("fuel_pump", "26fd"),
        Emoji("siren", "1f6a8"),
        Emoji("horizontal_traffic_light", "1f6a5"),
        Emoji("traffic_light", "1f6a6"),
        Emoji("stop_sign", "1f6d1"),
        Emoji("work_in_progress", "1f6a7"),
        Emoji("anchor", "2693"),
        Emoji("boat", "26f5"),
        Emoji("canoe", "1f6f6"),
        Emoji("speedboat", "1f6a4"),
        Emoji("passenger_ship", "1f6f3"),
        Emoji("ferry", "26f4"),
        Emoji("motor_boat", "1f6e5"),
        Emoji("ship", "1f6a2"),
        Emoji("airplane", "2708"),
        Emoji("small_airplane", "1f6e9"),
        Emoji("take_off", "1f6eb"),
        Emoji("landing", "1f6ec"),
        Emoji("seat", "1f4ba"),
        Emoji("helicopter", "1f681"),
        Emoji("suspension_railway", "1f69f"),
        Emoji("gondola", "1f6a0"),
        Emoji("aerial_tramway", "1f6a1"),
        Emoji("satellite", "1f6f0"),
        Emoji("rocket", "1f680"),
        Emoji("bellhop_bell", "1f6ce"),
        Emoji("times_up", "231b"),
        Emoji("time_ticking", "23f3"),
        Emoji("watch", "231a"),
        Emoji("alarm_clock", "23f0"),
        Emoji("stopwatch", "23f1"),
        Emoji("timer", "23f2"),
        Emoji("mantelpiece_clock", "1f570"),
        Emoji("time", "1f557"),
        Emoji("new_moon", "1f311"),
        Emoji("waxing_moon", "1f314"),
        Emoji("full_moon", "1f315"),
        Emoji("moon", "1f319"),
        Emoji("new_moon_face", "1f31a"),
        Emoji("goodnight", "1f31b"),
        Emoji("temperature", "1f321"),
        Emoji("sunny", "2600"),
        Emoji("moon_face", "1f31d"),
        Emoji("sun_face", "1f31e"),
        Emoji("star", "2b50"),
        Emoji("glowing_star", "1f31f"),
        Emoji("shooting_star", "1f320"),
        Emoji("milky_way", "1f30c"),
        Emoji("cloud", "2601"),
        Emoji("partly_sunny", "26c5"),
        Emoji("thunderstorm", "26c8"),
        Emoji("mostly_sunny", "1f324"),
        Emoji("cloudy", "1f325"),
        Emoji("sunshowers", "1f326"),
        Emoji("rainy", "1f327"),
        Emoji("snowy", "1f328"),
        Emoji("lightning", "1f329"),
        Emoji("tornado", "1f32a"),
        Emoji("fog", "1f32b"),
        Emoji("windy", "1f32c"),
        Emoji("cyclone", "1f300"),
        Emoji("rainbow", "1f308"),
        Emoji("closed_umbrella", "1f302"),
        Emoji("umbrella", "2602"),
        Emoji("umbrella_with_rain", "2614"),
        Emoji("beach_umbrella", "26f1"),
        Emoji("high_voltage", "26a1"),
        Emoji("snowflake", "2744"),
        Emoji("snowman", "2603"),
        Emoji("frosty", "26c4"),
        Emoji("comet", "2604"),
        Emoji("fire", "1f525"),
        Emoji("drop", "1f4a7"),
        Emoji("ocean", "1f30a"),

// Objects
        Emoji("glasses", "1f453"),
        Emoji("dark_sunglasses", "1f576"),
        Emoji("tie", "1f454"),
        Emoji("shirt", "1f455"),
        Emoji("jeans", "1f456"),
        Emoji("dress", "1f457"),
        Emoji("kimono", "1f458"),
        Emoji("bikini", "1f459"),
        Emoji("clothing", "1f45a"),
        Emoji("purse", "1f45b"),
        Emoji("handbag", "1f45c"),
        Emoji("pouch", "1f45d"),
        Emoji("shopping_bags", "1f6cd"),
        Emoji("backpack", "1f392"),
        Emoji("shoe", "1f45e"),
        Emoji("athletic_shoe", "1f45f"),
        Emoji("high_heels", "1f460"),
        Emoji("sandal", "1f461"),
        Emoji("boot", "1f462"),
        Emoji("crown", "1f451"),
        Emoji("hat", "1f452"),
        Emoji("top_hat", "1f3a9"),
        Emoji("graduate", "1f393"),
        Emoji("helmet", "26d1"),
        Emoji("prayer_beads", "1f4ff"),
        Emoji("lipstick", "1f484"),
        Emoji("ring", "1f48d"),
        Emoji("gem", "1f48e"),
        Emoji("mute", "1f507"),
        Emoji("speaker", "1f508"),
        Emoji("softer", "1f509"),
        Emoji("louder", "1f50a"),
        Emoji("loudspeaker", "1f4e2"),
        Emoji("megaphone", "1f4e3"),
        Emoji("horn", "1f4ef"),
        Emoji("notifications", "1f514"),
        Emoji("mute_notifications", "1f515"),
        Emoji("musical_score", "1f3bc"),
        Emoji("music", "1f3b5"),
        Emoji("musical_notes", "1f3b6"),
        Emoji("studio_microphone", "1f399"),
        Emoji("volume", "1f39a"),
        Emoji("control_knobs", "1f39b"),
        Emoji("microphone", "1f3a4"),
        Emoji("headphones", "1f3a7"),
        Emoji("radio", "1f4fb"),
        Emoji("saxophone", "1f3b7"),
        Emoji("guitar", "1f3b8"),
        Emoji("piano", "1f3b9"),
        Emoji("trumpet", "1f3ba"),
        Emoji("violin", "1f3bb"),
        Emoji("drum", "1f941"),
        Emoji("mobile_phone", "1f4f1"),
        Emoji("calling", "1f4f2"),
        Emoji("phone", "260e"),
        Emoji("landline", "1f4de"),
        Emoji("pager", "1f4df"),
        Emoji("fax", "1f4e0"),
        Emoji("battery", "1f50b"),
        Emoji("electric_plug", "1f50c"),
        Emoji("computer", "1f4bb"),
        Emoji("desktop_computer", "1f5a5"),
        Emoji("printer", "1f5a8"),
        Emoji("keyboard", "2328"),
        Emoji("computer_mouse", "1f5b1"),
        Emoji("trackball", "1f5b2"),
        Emoji("gold_record", "1f4bd"),
        Emoji("floppy_disk", "1f4be"),
        Emoji("cd", "1f4bf"),
        Emoji("dvd", "1f4c0"),
        Emoji("movie_camera", "1f3a5"),
        Emoji("film", "1f39e"),
        Emoji("projector", "1f4fd"),
        Emoji("action", "1f3ac"),
        Emoji("tv", "1f4fa"),
        Emoji("camera", "1f4f7"),
        Emoji("taking_a_picture", "1f4f8"),
        Emoji("video_camera", "1f4f9"),
        Emoji("vhs", "1f4fc"),
        Emoji("search", "1f50d"),
        Emoji("candle", "1f56f"),
        Emoji("light_bulb", "1f4a1"),
        Emoji("flashlight", "1f526"),
        Emoji("lantern", "1f3ee"),
        Emoji("decorative_notebook", "1f4d4"),
        Emoji("red_book", "1f4d5"),
        Emoji("book", "1f4d6"),
        Emoji("green_book", "1f4d7"),
        Emoji("blue_book", "1f4d8"),
        Emoji("orange_book", "1f4d9"),
        Emoji("books", "1f4da"),
        Emoji("notebook", "1f4d3"),
        Emoji("ledger", "1f4d2"),
        Emoji("receipt", "1f4c3"),
        Emoji("scroll", "1f4dc"),
        Emoji("document", "1f4c4"),
        Emoji("headlines", "1f4f0"),
        Emoji("newspaper", "1f5de"),
        Emoji("place_holder", "1f4d1"),
        Emoji("bookmark", "1f516"),
        Emoji("label", "1f3f7"),
        Emoji("money", "1f4b0"),
        Emoji("yen_banknotes", "1f4b4"),
        Emoji("dollar_bills", "1f4b5"),
        Emoji("euro_banknotes", "1f4b6"),
        Emoji("pound_notes", "1f4b7"),
        Emoji("losing_money", "1f4b8"),
        Emoji("credit_card", "1f4b3"),
        Emoji("stock_market", "1f4b9"),
        Emoji("email", "2709"),
        Emoji("e-mail", "1f4e7"),
        Emoji("mail_received", "1f4e8"),
        Emoji("mail_sent", "1f4e9"),
        Emoji("outbox", "1f4e4"),
        Emoji("inbox", "1f4e5"),
        Emoji("package", "1f4e6"),
        Emoji("mailbox", "1f4eb"),
        Emoji("closed_mailbox", "1f4ea"),
        Emoji("unread_mail", "1f4ec"),
        Emoji("inbox_zero", "1f4ed"),
        Emoji("mail_dropoff", "1f4ee"),
        Emoji("ballot_box", "1f5f3"),
        Emoji("pencil", "270f"),
        Emoji("fountain_pen", "1f58b"),
        Emoji("pen", "1f58a"),
        Emoji("paintbrush", "1f58c"),
        Emoji("crayon", "1f58d"),
        Emoji("memo", "1f4dd"),
        Emoji("briefcase", "1f4bc"),
        Emoji("organize", "1f4c1"),
        Emoji("folder", "1f4c2"),
        Emoji("sort", "1f5c2"),
        Emoji("calendar", "1f4c5"),
        Emoji("date", "1f4c6"),
        Emoji("spiral_notepad", "1f5d2"),
        Emoji("rolodex", "1f4c7"),
        Emoji("chart", "1f4c8"),
        Emoji("downwards_trend", "1f4c9"),
        Emoji("bar_chart", "1f4ca"),
        Emoji("clipboard", "1f4cb"),
        Emoji("push_pin", "1f4cc"),
        Emoji("pin", "1f4cd"),
        Emoji("paperclip", "1f4ce"),
        Emoji("office_supplies", "1f587"),
        Emoji("ruler", "1f4cf"),
        Emoji("carpenter_square", "1f4d0"),
        Emoji("scissors", "2702"),
        Emoji("archive", "1f5c3"),
        Emoji("file_cabinet", "1f5c4"),
        Emoji("wastebasket", "1f5d1"),
        Emoji("locked", "1f512"),
        Emoji("unlocked", "1f513"),
        Emoji("privacy", "1f50f"),
        Emoji("secure", "1f510"),
        Emoji("key", "1f511"),
        Emoji("secret", "1f5dd"),
        Emoji("hammer", "1f528"),
        Emoji("mine", "26cf"),
        Emoji("at_work", "2692"),
        Emoji("working_on_it", "1f6e0"),
        Emoji("dagger", "1f5e1"),
        Emoji("duel", "2694"),
        Emoji("gun", "1f52b"),
        Emoji("bow_and_arrow", "1f3f9"),
        Emoji("shield", "1f6e1"),
        Emoji("fixing", "1f527"),
        Emoji("nut_and_bolt", "1f529"),
        Emoji("gear", "2699"),
        Emoji("compression", "1f5dc"),
        Emoji("justice", "2696"),
        Emoji("link", "1f517"),
        Emoji("chains", "26d3"),
        Emoji("alchemy", "2697"),
        Emoji("science", "1f52c"),
        Emoji("telescope", "1f52d"),
        Emoji("satellite_antenna", "1f4e1"),
        Emoji("injection", "1f489"),
        Emoji("medicine", "1f48a"),
        Emoji("door", "1f6aa"),
        Emoji("bed", "1f6cf"),
        Emoji("living_room", "1f6cb"),
        Emoji("toilet", "1f6bd"),
        Emoji("shower", "1f6bf"),
        Emoji("bathtub", "1f6c1"),
        Emoji("shopping_cart", "1f6d2"),
        Emoji("smoking", "1f6ac"),
        Emoji("coffin", "26b0"),
        Emoji("funeral_urn", "26b1"),
        Emoji("rock_carving", "1f5ff"),

// Symbol
        Emoji("atm", "1f3e7"),
        Emoji("put_litter_in_its_place", "1f6ae"),
        Emoji("potable_water", "1f6b0"),
        Emoji("accessible", "267f"),
        Emoji("mens", "1f6b9"),
        Emoji("womens", "1f6ba"),
        Emoji("restroom", "1f6bb"),
        Emoji("baby_change_station", "1f6bc"),
        Emoji("wc", "1f6be"),
        Emoji("passport_control", "1f6c2"),
        Emoji("customs", "1f6c3"),
        Emoji("baggage_claim", "1f6c4"),
        Emoji("locker", "1f6c5"),
        Emoji("warning", "26a0"),
        Emoji("children_crossing", "1f6b8"),
        Emoji("no_entry", "26d4"),
        Emoji("prohibited", "1f6ab"),
        Emoji("no_bicycles", "1f6b3"),
        Emoji("no_smoking", "1f6ad"),
        Emoji("do_not_litter", "1f6af"),
        Emoji("non-potable_water", "1f6b1"),
        Emoji("no_pedestrians", "1f6b7"),
        Emoji("no_phones", "1f4f5"),
        Emoji("underage", "1f51e"),
        Emoji("radioactive", "2622"),
        Emoji("biohazard", "2623"),
        Emoji("up", "2b06"),
        Emoji("upper_right", "2197"),
        Emoji("right", "27a1"),
        Emoji("lower_right", "2198"),
        Emoji("down", "2b07"),
        Emoji("lower_left", "2199"),
        Emoji("left", "2b05"),
        Emoji("upper_left", "2196"),
        Emoji("up_down", "2195"),
        Emoji("left_right", "2194"),
        Emoji("reply", "21a9"),
        Emoji("forward", "21aa"),
        Emoji("heading_up", "2934"),
        Emoji("heading_down", "2935"),
        Emoji("clockwise", "1f503"),
        Emoji("counterclockwise", "1f504"),
        Emoji("back", "1f519"),
        Emoji("end", "1f51a"),
        Emoji("on", "1f51b"),
        Emoji("soon", "1f51c"),
        Emoji("top", "1f51d"),
        Emoji("place_of_worship", "1f6d0"),
        Emoji("atom", "269b"),
        Emoji("om", "1f549"),
        Emoji("star_of_david", "2721"),
        Emoji("wheel_of_dharma", "2638"),
        Emoji("yin_yang", "262f"),
        Emoji("cross", "271d"),
        Emoji("orthodox_cross", "2626"),
        Emoji("star_and_crescent", "262a"),
        Emoji("peace", "262e"),
        Emoji("menorah", "1f54e"),
        Emoji("aries", "2648"),
        Emoji("taurus", "2649"),
        Emoji("gemini", "264a"),
        Emoji("cancer", "264b"),
        Emoji("leo", "264c"),
        Emoji("virgo", "264d"),
        Emoji("libra", "264e"),
        Emoji("scorpius", "264f"),
        Emoji("sagittarius", "2650"),
        Emoji("capricorn", "2651"),
        Emoji("aquarius", "2652"),
        Emoji("pisces", "2653"),
        Emoji("ophiuchus", "26ce"),
        Emoji("shuffle", "1f500"),
        Emoji("repeat", "1f501"),
        Emoji("repeat_one", "1f502"),
        Emoji("play", "25b6"),
        Emoji("fast_forward", "23e9"),
        Emoji("next_track", "23ed"),
        Emoji("play_pause", "23ef"),
        Emoji("play_reverse", "25c0"),
        Emoji("rewind", "23ea"),
        Emoji("previous_track", "23ee"),
        Emoji("upvote", "1f53c"),
        Emoji("double_up", "23eb"),
        Emoji("downvote", "1f53d"),
        Emoji("double_down", "23ec"),
        Emoji("pause", "23f8"),
        Emoji("stop_button", "23f9"),
        Emoji("record", "23fa"),
        Emoji("cinema", "1f3a6"),
        Emoji("low_brightness", "1f505"),
        Emoji("brightness", "1f506"),
        Emoji("cell_reception", "1f4f6"),
        Emoji("vibration_mode", "1f4f3"),
        Emoji("phone_off", "1f4f4"),
        Emoji("multiplication", "2716"),
        Emoji("plus", "2795"),
        Emoji("minus", "2796"),
        Emoji("division", "2797"),
        Emoji("bangbang", "203c"),
        Emoji("interrobang", "2049"),
        Emoji("question", "2753"),
        Emoji("grey_question", "2754"),
        Emoji("grey_exclamation", "2755"),
        Emoji("exclamation", "2757"),
        Emoji("wavy_dash", "3030"),
        Emoji("exchange", "1f4b1"),
        Emoji("dollars", "1f4b2"),
        Emoji("recycle", "267b"),
        Emoji("fleur_de_lis", "269c"),
        Emoji("trident", "1f531"),
        Emoji("name_badge", "1f4db"),
        Emoji("beginner", "1f530"),
        Emoji("circle", "2b55"),
        Emoji("check", "2705"),
        Emoji("checkbox", "2611"),
        Emoji("check_mark", "2714"),
        Emoji("cross_mark", "274c"),
        Emoji("x", "274e"),
        Emoji("loop", "27b0"),
        Emoji("double_loop", "27bf"),
        Emoji("part_alternation", "303d"),
        Emoji("eight_spoked_asterisk", "2733"),
        Emoji("eight_pointed_star", "2734"),
        Emoji("sparkle", "2747"),
        Emoji("tm", "2122"),
        Emoji("hash", "0023"),
        Emoji("asterisk", "002a"),
        Emoji("zero", "0030"),
        Emoji("one", "0031"),
        Emoji("two", "0032"),
        Emoji("three", "0033"),
        Emoji("four", "0034"),
        Emoji("five", "0035"),
        Emoji("six", "0036"),
        Emoji("seven", "0037"),
        Emoji("eight", "0038"),
        Emoji("nine", "0039"),
        Emoji("ten", "1f51f"),
        Emoji("capital_abcd", "1f520"),
        Emoji("abcd", "1f521"),
        Emoji("1234", "1f522"),
        Emoji("symbols", "1f523"),
        Emoji("abc", "1f524"),
        Emoji("a", "1f170"),
        Emoji("ab", "1f18e"),
        Emoji("b", "1f171"),
        Emoji("cl", "1f191"),
        Emoji("cool", "1f192"),
        Emoji("free", "1f193"),
        Emoji("info", "2139"),
        Emoji("id", "1f194"),
        Emoji("metro", "24c2"),
        Emoji("new", "1f195"),
        Emoji("ng", "1f196"),
        Emoji("o", "1f17e"),
        Emoji("squared_ok", "1f197"),
        Emoji("parking", "1f17f"),
        Emoji("sos", "1f198"),
        Emoji("squared_up", "1f199"),
        Emoji("vs", "1f19a"),
        Emoji("red_circle", "1f534"),
        Emoji("blue_circle", "1f535"),
        Emoji("black_circle", "26ab"),
        Emoji("white_circle", "26aa"),
        Emoji("black_large_square", "2b1b"),
        Emoji("white_large_square", "2b1c"),
        Emoji("black_medium_square", "25fc"),
        Emoji("white_medium_square", "25fb"),
        Emoji("black_medium_small_square", "25fe"),
        Emoji("white_medium_small_square", "25fd"),
        Emoji("black_small_square", "25aa"),
        Emoji("white_small_square", "25ab"),
        Emoji("large_orange_diamond", "1f536"),
        Emoji("large_blue_diamond", "1f537"),
        Emoji("small_orange_diamond", "1f538"),
        Emoji("small_blue_diamond", "1f539"),
        Emoji("red_triangle_up", "1f53a"),
        Emoji("red_triangle_down", "1f53b"),
        Emoji("cute", "1f4a0"),
        Emoji("radio_button", "1f518"),
        Emoji("black_and_white_square", "1f533"),
        Emoji("white_and_black_square", "1f532"),

// Flags
        Emoji("checkered_flag", "1f3c1"),
        Emoji("triangular_flag", "1f6a9"),
        Emoji("crossed_flags", "1f38c"),
        Emoji("black_flag", "1f3f4"),
        Emoji("white_flag", "1f3f3")
    )
}