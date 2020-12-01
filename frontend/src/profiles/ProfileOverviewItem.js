import React from "react";
import Card from '@material-ui/core/Card';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import makeStyles from "@material-ui/core/styles/makeStyles";
import {useHistory} from "react-router-dom";
import SkillList from "./SkillList";
import {Avatar} from "@material-ui/core";

export default function ProfileOverviewItem({profile}) {

    const classes = useStyles();
    const history = useHistory();

    return (
        <Card className={classes.root} variant="elevation">
            <CardContent>
                {profile.imageUrl && <Avatar src={profile.imageUrl}/>}
                <Typography variant="h5" component="h2">
                    {profile.name}
                </Typography>
                <Typography className={classes.pos} color="textSecondary">
                    {profile.location}
                </Typography>
                <Typography variant="body2" component="p">
                    {profile.birthday}
                </Typography>
                <SkillList skills={profile.skills}/>
            </CardContent>
            <CardActions>
                <Button className={classes.button} size="small" onClick={() => history.push(`/profile/${profile.username}`)}>Learn More</Button>
            </CardActions>
        </Card>
    );
}

const useStyles = makeStyles({
    root: {
        minWidth: 275,
        backgroundColor: "#FFF4F4",
        borderRadius: "20px"
    },
    button: {
        backgroundColor: "#9F0D2F",
        color: "white",
        borderRadius: "10px",
        fontSize: "0.8em",
        padding: "8px"
    },
    name: {
        fontSize: 20
    },
    bullet: {
        display: 'inline-block',
        margin: '0 2px',
        transform: 'scale(0.8)',
    },
    title: {
        fontSize: 14,
    },
    pos: {
        marginBottom: 12,
    },
});


