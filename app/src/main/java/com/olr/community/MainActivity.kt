package com.olr.community

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF101214)
private val Panel = Color(0xFF1A1E20)
private val PanelRaised = Color(0xFF23282B)
private val Orange = Color(0xFFFF6B24)
private val Paper = Color(0xFFF3F1EC)
private val Muted = Color(0xFF9BA2A4)
private val Mint = Color(0xFF83D5B0)

private data class FeedPost(val author: String, val car: String, val message: String, val likes: Int)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(16, 18, 20)
        window.navigationBarColor = android.graphics.Color.rgb(16, 18, 20)
        setContent { OLRCommunityApp() }
    }
}

@Composable
private fun OLRCommunityApp() {
    var selectedTab by rememberSaveable { mutableStateOf(0) }
    var isRegistered by rememberSaveable { mutableStateOf(false) }
    var driverName by rememberSaveable { mutableStateOf("Alex Morgan") }
    var driverNumber by rememberSaveable { mutableStateOf("27") }
    var editingProfile by rememberSaveable { mutableStateOf(false) }
    var draftPost by rememberSaveable { mutableStateOf("") }
    val posts = remember {
        mutableStateListOf(
            FeedPost("Mia Chen", "Porsche 911 GT3 R", "First podium of the season! What a battle through the final sector.", 24),
            FeedPost("Luca Rossi", "BMW M4 GT3", "Track walk at Zandvoort is done. That banking never gets old.", 18),
            FeedPost("Sam Taylor", "Ferrari 296 GT3", "Looking for a teammate for the endurance round. Clean racing only.", 12),
        )
    }
    val likedPosts = remember { mutableStateListOf<Int>() }

    MaterialTheme {
        Scaffold(
            containerColor = Ink,
            topBar = { AppHeader() },
            bottomBar = {
                NavigationBar(containerColor = Panel, contentColor = Paper) {
                    val items = listOf(
                        "Home" to Icons.Filled.Home,
                        "Races" to Icons.Filled.Flag,
                        "Standings" to Icons.Filled.EmojiEvents,
                        "Community" to Icons.AutoMirrored.Filled.Chat,
                        "Profile" to Icons.Filled.Person,
                    )
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            icon = { Icon(item.second, contentDescription = item.first, modifier = Modifier.size(21.dp)) },
                            label = { Text(item.first, fontSize = 10.sp, maxLines = 1) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Orange,
                                selectedTextColor = Orange,
                                indicatorColor = Orange.copy(alpha = 0.12f),
                                unselectedIconColor = Muted,
                                unselectedTextColor = Muted,
                            ),
                        )
                    }
                }
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
            ) {
                Spacer(Modifier.height(20.dp))
                when (selectedTab) {
                    0 -> HomeScreen(isRegistered) { isRegistered = !isRegistered }
                    1 -> RacesScreen(isRegistered) { isRegistered = !isRegistered }
                    2 -> StandingsScreen()
                    3 -> CommunityScreen(posts, likedPosts, draftPost, { draftPost = it }) {
                        if (draftPost.isNotBlank()) {
                            posts.add(0, FeedPost(driverName, "#${driverNumber} · OLR Driver", draftPost.trim(), 0))
                            draftPost = ""
                        }
                    }
                    else -> ProfileScreen(
                        driverName = driverName,
                        driverNumber = driverNumber,
                        editing = editingProfile,
                        onEditToggle = { editingProfile = !editingProfile },
                        onNameChange = { driverName = it },
                        onNumberChange = { driverNumber = it },
                    )
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun AppHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Ink)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Orange),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Filled.Speed, contentDescription = null, tint = Ink, modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.width(10.dp))
        Column {
            Text("OLR", color = Paper, fontSize = 16.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
            Text("COMMUNITY", color = Muted, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.4.sp)
        }
        Spacer(Modifier.weight(1f))
        Surface(color = Panel, shape = RoundedCornerShape(20.dp)) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(Modifier.size(7.dp).clip(CircleShape).background(Mint))
                Spacer(Modifier.width(6.dp))
                Text("SEASON 04", color = Paper, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun HomeScreen(isRegistered: Boolean, onRegister: () -> Unit) {
    Text("SATURDAY, 03 OCTOBER", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
    Spacer(Modifier.height(5.dp))
    Text("Race your\ncommunity.", color = Paper, fontSize = 32.sp, lineHeight = 35.sp, fontWeight = FontWeight.Black)
    Spacer(Modifier.height(20.dp))
    SectionEyebrow("NEXT UP", "ROUND 04  /  SPRINT CUP")
    EventSpotlight(isRegistered, onRegister)
    Spacer(Modifier.height(20.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        StatTile("128", "DRIVERS", Modifier.weight(1f))
        StatTile("12", "RACE WEEKS", Modifier.weight(1f))
        StatTile("04", "CHAMPIONSHIPS", Modifier.weight(1f))
    }
    Spacer(Modifier.height(22.dp))
    SectionEyebrow("THE PADDOCK", "LIVE IN THE COMMUNITY")
    Surface(color = Panel, shape = RoundedCornerShape(14.dp)) {
        Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Avatar("MC", Orange)
            Spacer(Modifier.width(11.dp))
            Column(Modifier.weight(1f)) {
                Text("Mia Chen", color = Paper, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("Shared a podium moment", color = Muted, fontSize = 11.sp)
            }
            Icon(Icons.Filled.ThumbUp, contentDescription = null, tint = Orange, modifier = Modifier.size(17.dp))
            Spacer(Modifier.width(5.dp))
            Text("24", color = Muted, fontSize = 11.sp)
        }
    }
}

@Composable
private fun EventSpotlight(isRegistered: Boolean, onRegister: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Panel)
            .border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(16.dp))
            .padding(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(color = Orange.copy(alpha = 0.15f), shape = RoundedCornerShape(5.dp)) {
                Text("SPRINT", modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp), color = Orange, fontSize = 9.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
            }
            Spacer(Modifier.weight(1f))
            Text("03 OCT  ·  19:30 UTC", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(15.dp))
        Text("Zandvoort GP", color = Paper, fontSize = 24.sp, fontWeight = FontWeight.Black)
        Text("Circuit Zandvoort  ·  2 × 25 min", color = Muted, fontSize = 12.sp)
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Flag, contentDescription = null, tint = Orange, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("GT3  ·  32 GRID SLOTS", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            Button(
                onClick = onRegister,
                colors = ButtonDefaults.buttonColors(containerColor = if (isRegistered) PanelRaised else Orange, contentColor = if (isRegistered) Mint else Ink),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(if (isRegistered) "ENTERED ✓" else "JOIN RACE", fontSize = 10.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
private fun RacesScreen(isRegistered: Boolean, onRegister: () -> Unit) {
    ScreenHeading("Races & Events", "Your next green light is closer than you think.")
    SectionEyebrow("UPCOMING", "OCTOBER 2026")
    EventRow("03 OCT", "Zandvoort GP", "Sprint Cup · GT3 · 19:30 UTC", "32 GRID SLOTS", isRegistered, onRegister)
    EventRow("10 OCT", "Spa-Francorchamps", "Endurance · GT3 · 18:00 UTC", "DRIVER SWAP", false) {}
    EventRow("17 OCT", "Monza", "Club race · GT4 · 20:00 UTC", "OPEN LOBBY", false) {}
    Spacer(Modifier.height(22.dp))
    SectionEyebrow("RACE WEEKEND", "ROUND 04 FORMAT")
    InfoLine("18:45 UTC", "Practice opens", "45-minute open practice")
    InfoLine("19:20 UTC", "Qualifying", "15 minutes · Flying lap")
    InfoLine("19:30 UTC", "Sprint race", "2 races · 25 minutes each")
}

@Composable
private fun EventRow(date: String, title: String, detail: String, tag: String, joined: Boolean, onJoin: () -> Unit) {
    Surface(color = Panel, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
        Row(Modifier.padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.width(51.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(date.substringBefore(" "), color = Orange, fontSize = 20.sp, fontWeight = FontWeight.Black)
                Text(date.substringAfter(" "), color = Muted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = Paper, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(detail, color = Muted, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(4.dp))
                Text(if (joined) "YOU'RE ON THE GRID" else tag, color = if (joined) Mint else Orange, fontSize = 8.sp, fontWeight = FontWeight.Black, letterSpacing = 0.7.sp)
            }
            if (date.startsWith("18")) {
                TextButton(onClick = onJoin) { Text(if (joined) "JOINED" else "JOIN", color = if (joined) Mint else Orange, fontSize = 9.sp, fontWeight = FontWeight.Black) }
            } else {
                Icon(Icons.Filled.CalendarMonth, contentDescription = null, tint = Muted, modifier = Modifier.size(19.dp))
            }
        }
    }
}

@Composable
private fun StandingsScreen() {
    ScreenHeading("Championships", "Season 04 · GT World Series")
    Row(Modifier.fillMaxWidth().padding(bottom = 14.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf("GT WORLD" to true, "SPRINT CUP" to false, "ENDURANCE" to false).forEach { (label, selected) ->
            Surface(color = if (selected) Orange else Panel, shape = RoundedCornerShape(7.dp)) {
                Text(label, color = if (selected) Ink else Muted, modifier = Modifier.padding(horizontal = 10.dp, vertical = 9.dp), fontSize = 8.sp, fontWeight = FontWeight.Black, letterSpacing = 0.4.sp)
            }
        }
    }
    Surface(color = Panel, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(horizontal = 14.dp, vertical = 6.dp)) {
            Row(Modifier.fillMaxWidth().padding(vertical = 11.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("POS", color = Muted, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(34.dp))
                Text("DRIVER", color = Muted, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text("WINS", color = Muted, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(38.dp))
                Text("PTS", color = Muted, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(34.dp))
            }
            listOf(
                listOf("01", "Mia Chen", "04", "142"),
                listOf("02", "Luca Rossi", "03", "128"),
                listOf("03", "Alex Morgan", "02", "116"),
                listOf("04", "Sam Taylor", "01", "104"),
                listOf("05", "Noah Williams", "01", "097"),
                listOf("06", "Jules Martin", "00", "089"),
                listOf("07", "Kai Nakamura", "00", "076"),
                listOf("08", "Ava Johnson", "00", "068"),
            ).forEach { StandingRow(it[0], it[1], it[2], it[3], it[1] == "Alex Morgan") }
        }
    }
    Spacer(Modifier.height(16.dp))
    Surface(color = Panel, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = Orange, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(11.dp))
            Column(Modifier.weight(1f)) {
                Text("Season finale", color = Paper, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text("6 rounds complete · 4 to go", color = Muted, fontSize = 10.sp)
            }
            Text("60%", color = Orange, fontSize = 14.sp, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun StandingRow(position: String, name: String, wins: String, points: String, isYou: Boolean) {
    Row(
        Modifier.fillMaxWidth().background(if (isYou) Orange.copy(alpha = 0.08f) else Color.Transparent).padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(position, color = if (position == "01") Orange else Muted, fontSize = 11.sp, fontWeight = FontWeight.Black, modifier = Modifier.width(34.dp))
        Text(name + if (isYou) "  YOU" else "", color = if (isYou) Orange else Paper, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(wins, color = Muted, fontSize = 11.sp, modifier = Modifier.width(38.dp))
        Text(points, color = Paper, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(34.dp))
    }
}

@Composable
private fun CommunityScreen(
    posts: List<FeedPost>,
    likedPosts: MutableList<Int>,
    draftPost: String,
    onDraftChange: (String) -> Unit,
    onPost: () -> Unit,
) {
    ScreenHeading("The Paddock", "Good racing starts with good people.")
    Surface(color = Panel, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Avatar("AM", Orange)
            Spacer(Modifier.width(10.dp))
            Box(Modifier.weight(1f)) {
                if (draftPost.isEmpty()) Text("Share something with the grid...", color = Muted, fontSize = 11.sp)
                BasicTextField(value = draftPost, onValueChange = onDraftChange, modifier = Modifier.fillMaxWidth(), textStyle = androidx.compose.ui.text.TextStyle(color = Paper, fontSize = 11.sp), maxLines = 3)
            }
            Spacer(Modifier.width(8.dp))
            Surface(
                color = if (draftPost.isBlank()) PanelRaised else Orange,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.size(36.dp).clickable(enabled = draftPost.isNotBlank(), onClick = onPost),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Post message", tint = if (draftPost.isBlank()) Muted else Ink, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
    Spacer(Modifier.height(18.dp))
    SectionEyebrow("LATEST FROM DRIVERS", "${posts.size} POSTS")
    posts.forEachIndexed { index, post ->
        PostCard(post, likedPosts.contains(index)) {
            if (likedPosts.contains(index)) likedPosts.remove(index) else likedPosts.add(index)
        }
    }
}

@Composable
private fun PostCard(post: FeedPost, liked: Boolean, onLike: () -> Unit) {
    Surface(color = Panel, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Avatar(post.author.take(1) + post.author.substringAfter(" ", " ").take(1), Orange.copy(alpha = 0.85f))
                Spacer(Modifier.width(9.dp))
                Column(Modifier.weight(1f)) {
                    Text(post.author, color = Paper, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(post.car, color = Muted, fontSize = 9.sp)
                }
                Icon(Icons.Filled.Tune, contentDescription = "More options", tint = Muted, modifier = Modifier.size(17.dp))
            }
            Spacer(Modifier.height(12.dp))
            Text(post.message, color = Paper.copy(alpha = 0.9f), fontSize = 12.sp, lineHeight = 18.sp)
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable(onClick = onLike)) {
                Icon(Icons.Filled.ThumbUp, contentDescription = if (liked) "Unlike" else "Like", tint = if (liked) Orange else Muted, modifier = Modifier.size(15.dp))
                Spacer(Modifier.width(6.dp))
                Text("${post.likes + if (liked) 1 else 0} likes", color = if (liked) Orange else Muted, fontSize = 10.sp)
                Spacer(Modifier.width(17.dp))
                Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, tint = Muted, modifier = Modifier.size(15.dp))
                Spacer(Modifier.width(5.dp))
                Text("Reply", color = Muted, fontSize = 10.sp)
            }
        }
    }
}

@Composable
private fun ProfileScreen(
    driverName: String,
    driverNumber: String,
    editing: Boolean,
    onEditToggle: () -> Unit,
    onNameChange: (String) -> Unit,
    onNumberChange: (String) -> Unit,
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        ScreenHeading("Driver Profile", "Your place on the grid.", Modifier.weight(1f))
        TextButton(onClick = onEditToggle) {
            Icon(Icons.Filled.Settings, contentDescription = null, tint = Orange, modifier = Modifier.size(15.dp))
            Spacer(Modifier.width(5.dp))
            Text(if (editing) "SAVE" else "EDIT", color = Orange, fontSize = 10.sp, fontWeight = FontWeight.Black)
        }
    }
    Surface(color = Panel, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(76.dp).clip(CircleShape).background(Orange), contentAlignment = Alignment.Center) {
                Text(driverNumber, color = Ink, fontSize = 27.sp, fontWeight = FontWeight.Black)
            }
            Spacer(Modifier.height(12.dp))
            if (editing) {
                ProfileField("DRIVER NAME", driverName, onNameChange)
                Spacer(Modifier.height(9.dp))
                ProfileField("RACE NUMBER", driverNumber, onNumberChange)
            } else {
                Text(driverName, color = Paper, fontSize = 22.sp, fontWeight = FontWeight.Black)
                Text("OLR CLUB DRIVER  ·  SEASON 04", color = Muted, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.7.sp)
            }
            Spacer(Modifier.height(18.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                ProfileStat("116", "POINTS")
                ProfileStat("02", "WINS")
                ProfileStat("08", "PODIUMS")
            }
        }
    }
    Spacer(Modifier.height(20.dp))
    SectionEyebrow("DRIVER DETAILS", "MEMBER SINCE 2024")
    InfoLine("CLASS", "GT3 · Silver", "Promoted after Round 02")
    InfoLine("TEAM", "Apex Motorsport", "#27 · Porsche 911 GT3 R")
    InfoLine("RATING", "2,486 ELO", "Top 12% of all drivers")
    Spacer(Modifier.height(14.dp))
    Surface(color = Panel, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = Orange, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(10.dp))
            Column {
                Text("Season 04 championship", color = Paper, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text("P3 overall · 26 points to P2", color = Muted, fontSize = 10.sp)
            }
        }
    }
}

@Composable
private fun ProfileField(label: String, value: String, onChange: (String) -> Unit) {
    Column(Modifier.fillMaxWidth()) {
        Text(label, color = Muted, fontSize = 8.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        BasicTextField(
            value = value,
            onValueChange = onChange,
            modifier = Modifier.fillMaxWidth().padding(top = 5.dp).background(PanelRaised, RoundedCornerShape(6.dp)).padding(9.dp),
            textStyle = androidx.compose.ui.text.TextStyle(color = Paper, fontSize = 13.sp, fontWeight = FontWeight.Bold),
            singleLine = true,
        )
    }
}

@Composable
private fun ProfileStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = Paper, fontSize = 19.sp, fontWeight = FontWeight.Black)
        Text(label, color = Muted, fontSize = 8.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
    }
}

@Composable
private fun ScreenHeading(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Column(modifier.padding(bottom = 20.dp)) {
        Text(title, color = Paper, fontSize = 25.sp, fontWeight = FontWeight.Black)
        Spacer(Modifier.height(4.dp))
        Text(subtitle, color = Muted, fontSize = 11.sp)
    }
}

@Composable
private fun SectionEyebrow(title: String, detail: String) {
    Row(Modifier.fillMaxWidth().padding(bottom = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(title, color = Paper, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 0.8.sp)
        Spacer(Modifier.weight(1f))
        Text(detail, color = Muted, fontSize = 8.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.4.sp)
    }
}

@Composable
private fun StatTile(value: String, label: String, modifier: Modifier = Modifier) {
    Surface(color = Panel, shape = RoundedCornerShape(10.dp), modifier = modifier) {
        Column(Modifier.padding(vertical = 12.dp, horizontal = 10.dp)) {
            Text(value, color = Orange, fontSize = 20.sp, fontWeight = FontWeight.Black)
            Text(label, color = Muted, fontSize = 8.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.4.sp, maxLines = 1)
        }
    }
}

@Composable
private fun Avatar(initials: String, color: Color) {
    Box(Modifier.size(34.dp).clip(CircleShape).background(color.copy(alpha = 0.18f)), contentAlignment = Alignment.Center) {
        Text(initials, color = Orange, fontSize = 10.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun InfoLine(time: String, title: String, detail: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(time, color = Orange, fontSize = 10.sp, fontWeight = FontWeight.Black, modifier = Modifier.width(76.dp))
        Column {
            Text(title, color = Paper, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(detail, color = Muted, fontSize = 9.sp)
        }
    }
}