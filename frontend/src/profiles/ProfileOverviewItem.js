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
                {profile.imageUrl && <Avatar alt="profile image" src={profile.imageUrl}/>}
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

/*
https://examplebucket.s3.amazonaws.com/test.txt
// ?X-Amz-Algorithm=AWS4-HMAC-SHA256
// &X-Amz-Credential=AKIAIOSFODNN7EXAMPLE%2F20130524%2Fus-east-1%2Fs3%2Faws4_request
// &X-Amz-Date=20130524T000000Z&X-Amz-Expires=86400
// &X-Amz-SignedHeaders=host
// &X-Amz-Signature=aeeed9bbccd4d02ee5c0109b86d86835f995330da4c265957d157751f604d404
*/
