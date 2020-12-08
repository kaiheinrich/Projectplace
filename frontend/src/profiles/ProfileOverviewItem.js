import React from "react";
import Card from '@material-ui/core/Card';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import Button from '@material-ui/core/Button';
import makeStyles from "@material-ui/core/styles/makeStyles";
import {useHistory} from "react-router-dom";
import SkillList from "./SkillList";
import {Avatar, CardHeader} from "@material-ui/core";

export default function ProfileOverviewItem({profile}) {

    const classes = useStyles();
    const history = useHistory();

    return (
        <Card className={classes.root} variant="elevation" elevation={20}>
            <CardHeader
                className={classes.cardHeader}
                title={profile.name}
                subheader={profile.location}
                action={profile.imageUrl ? <Avatar className={classes.avatar} src={profile.imageUrl}/> : <Avatar className={classes.avatar}/>}/>
            <CardContent>
                <SkillList skills={profile.skills}/>
            </CardContent>
            <CardActions className={classes.buttonPosition}>
                <Button className={classes.button} size="small" onClick={() => history.push(`/profile/${profile.username}`)}>See more</Button>
                <Button className={classes.button} size="small" onClick={() => history.push(`/messageto/${profile.username}`)}>Get in touch</Button>
            </CardActions>
        </Card>
    );
}

const useStyles = makeStyles({
    root: {
        minWidth: 275,
        maxWidth: 300,
        backgroundColor: "#FFFFFF",
        borderRadius: "5px"
    },
    cardHeader: {
        backgroundColor: "#F3EED9"
    },
    button: {
        backgroundColor: "#e7e7e7",
        color: "black",
        width: "150px",
        borderRadius: "10px",
        fontSize: "0.9em",
        padding: "8px"
    },
    buttonPosition: {
        justifyContent: "center",
        alignContent: "center",
        padding: "0px 16px 16px 16px"
    },
    avatar: {
        width: "70px",
        height: "70px"
    }
});
