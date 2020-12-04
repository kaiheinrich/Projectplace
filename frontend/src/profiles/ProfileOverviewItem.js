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
import styled from "styled-components/macro";
import Box from "@material-ui/core/Box";

export default function ProfileOverviewItem({profile}) {

    const classes = useStyles();
    const history = useHistory();

    return (
        <Card className={classes.root} variant="elevation">
            <CardContent>
                <DivStyled>
                    <div>
                        <Typography variant="h5" component="h2">
                            {profile.name}
                        </Typography>
                        <Typography className={classes.pos} color="textSecondary">
                            Location: {profile.location}
                        </Typography>
                    </div>
                    <Box component={'div'} className={classes.avatarPosition}>
                        {profile.imageUrl && <Avatar className={classes.avatar} src={profile.imageUrl}/>}
                    </Box>
                </DivStyled>
                <SkillList skills={profile.skills}/>
            </CardContent>
            <CardActions className={classes.buttonPosition}>
                <Button className={classes.button} size="small" onClick={() => history.push(`/profile/${profile.username}`)}>Check out dude</Button>
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
        fontSize: "0.9em",
        padding: "8px"
    },
    buttonPosition: {
        justifyContent: "center"
    },
    avatar: {
        width: "60px",
        height: "60px"
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
        marginBottom: 20,
    },
    avatarPosition: {
        alignSelf: "start",
        justifySelf: "end"
    }
});

const DivStyled = styled.div`
  display: grid;
  grid-template-columns: 1fr 1fr;
`